package com.sadatmalik.fusionweb.oauth.hsbc;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.bc.BouncyCastleFIPSProviderSingleton;
import com.sadatmalik.fusionweb.config.JwtProperties;
import com.sadatmalik.fusionweb.config.OauthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.concurrent.TimeUnit;


@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
@Service
public final class JwtHelper {

    private final JwtProperties properties;
    private final OauthConfig oauthConfig;

    // JWT -- header: {"alg":"PS256","typ":"JWT","kid":"7fab807d-4988-4012-8f10-a77655787450"}
    // JWT -- payload: {"iss":"https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2",
    //                  "aud":"211e36de-64b2-479e-ae28-8a5b41a1a940",
    //                  "response_type":"code id_token",
    //                  "client_id":"211e36de-64b2-479e-ae28-8a5b41a1a940",
    //                  "redirect_uri":"{{insert_your_app_url} }",
    //                  "scope":"openid accounts",
    //                  "claims":
    //                    {"userinfo":
    //                      {"openbanking_intent_id":
    //                        {"value":"{{insert_consent_id_retrieved_from_call#2}}",
    //                        "essential":true} }}}
    // JWT -- signature: private key
    public JWSObject createJwt(HsbcConsent consent) {

        PrivateKey privateKey = null;
        try {
            privateKey = getPrivateKey(properties.getKey());
        } catch (Exception e) {
            log.error("Error creating JWT - could not load private key");
            e.printStackTrace();
        }

        // Create RSA signer and set BC FIPS provider
        // See https://connect2id.com/products/nimbus-jose-jwt/examples/jws-ps256-with-bouncycastle-fips
        JWSSigner signer = new RSASSASigner(privateKey);
        signer.getJCAContext().setProvider(BouncyCastleFIPSProviderSingleton.getInstance());

        // Payload
        String payload = "{\"iss\":\"https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2\",";
        payload += "\"aud\":\"211e36de-64b2-479e-ae28-8a5b41a1a940\",";
        payload += "\"response_type\":\"code id_token\",";
        payload += "\"client_id\":\"211e36de-64b2-479e-ae28-8a5b41a1a940\",";
        payload += "\"redirect_uri\":\"" + oauthConfig.getAppRedirectUrl() + "\",";
        payload += "\"scope\":\"openid accounts\",";
        payload += "\"claims\":";
        payload += "{\"userinfo\":";
        payload += "{\"openbanking_intent_id\":";
        payload += "{\"value\":\"" + consent.getConsentID() + "\",";
        payload += "\"essential\":true} }}}";
        log.debug(payload);

        // Create and sign
        log.debug("Creating JWSObject");
        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.PS256)
                        .keyID("7fab807d-4988-4012-8f10-a77655787450")
                        .type(JOSEObjectType.JWT)
                        .build(),
                new Payload(payload));
        log.debug("Created JWSObject");

        try {
            log.debug("Signing JWSObject");
            TimeUnit.SECONDS.sleep(1);
            jwsObject.sign(signer);
            log.debug("Signed JWSObject");
        } catch (JOSEException | InterruptedException e) {
            log.error("Error creating JWT - could not sign JWT");
            e.printStackTrace();
        }

        return jwsObject;
    }

    private static PrivateKey getPrivateKey(String filename) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}
