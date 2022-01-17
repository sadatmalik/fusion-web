package com.sadatmalik.fusionweb.services;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.crypto.bc.BouncyCastleFIPSProviderSingleton;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.interfaces.RSAPublicKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HsbcServiceTest {

    private HsbcAccessToken accessToken;
    private HsbcConsent consent;
    private String authorizationURL;

    @Autowired
    private HsbcService service;

    @Autowired
    private  JwtHelper jwtHelper;

    @Test
    void testGetAccessToken() {
        accessToken = service.getAccessToken();
    }

    @Test
    void testGetConsentId() {
        accessToken = service.getAccessToken();
        consent = service.getConsentID(accessToken);
    }

    @Test
    void testCreateNimbusJwt() {
        HsbcConsent consent = new HsbcConsent();
        consent.data = new HsbcConsentData();
        consent.data.consentId = "9794df16-5fb8-43e9-b8f7-579ce706b3dd";

        JWSObject jws = jwtHelper.createJwt(consent);
        System.out.println(jws.getHeader());
        System.out.println(jws.getPayload());
        System.out.println(jws.serialize());
    }

    @Test
    void testGetAuthorizationURL() {
        accessToken = service.getAccessToken();
        consent = service.getConsentID(accessToken);
        authorizationURL = service.getAuthorizationURL(consent);
        System.out.println(authorizationURL);
    }

}