package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

@Service
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class HsbcService {

    private static final Logger logger = LoggerFactory.getLogger(HsbcService.class);
    private static final String APP_REDIRECT_URL = "http://google.com";
    private final JwtProperties properties;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private AuthorizationCodeTokenService tokenService;



    // curl -v -X POST --cert qwac.der --cert-type DER --key server.key
    // -H "Content-Type: application/x-www-form-urlencoded"
    // -H "Accept: application/json"
    // -H "x-fapi-financial-id: test"
    // -H "Cache-Control: no-cache"
    // -d 'grant_type=client_credentials
    // &scope=accounts
    // &client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer
    // &client_assertion=eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkZThjYTc3LWQ2ODEtNDc4Mi04MTIyLWUwMzkyNTg5MDIxYiJ9.eyJpc3MiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJhdWQiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwic3ViIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwiaWF0IjoxNDk5MTgzNjAxLCJleHAiOjE3NzkzNDg1MjF9.uu282OmEHUa0t6z6T68MfXzEGGgq8PiWuyJxuNQ1be6iWdD5sVbw3W--_O6TFAH-ae7BYXsE0kncYgA6gF9AmkXuA77w_Wbn2YyjPCB9gDCkrlJUS6rvb3UJYcIBZ7W-WZlRAsRE0l6EV74c5xnyL9c7cpGMfQ-HfPsYOG4JCsrvtpAHdo7jHWTVgKoe67jWGQkNOYt1Ba7rCf4y-fqQ3d6hZoptAAcJd26yigvV4768GHQGrBvgAc7OzutOGzYARAgStpjQMp0kMiOGIzq-TUsDlvtMrx2fH8gfy2uG2HvzsROkbNedL-iO5PmswNrDvCYEWZmVjMcaVg--ZF0sjg'
    // "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2"
    public HsbcAccessToken getAccessToken() {

        String access_token_url = "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2";

        String grant_type = "client_credentials";
        String scope = "accounts";
        String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
        String client_assertion = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkZThjYTc3LWQ2ODEtNDc4Mi04MTIyLWUwMzkyNTg5MDIxYiJ9.eyJpc3MiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJhdWQiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwic3ViIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwiaWF0IjoxNDk5MTgzNjAxLCJleHAiOjE3NzkzNDg1MjF9.uu282OmEHUa0t6z6T68MfXzEGGgq8PiWuyJxuNQ1be6iWdD5sVbw3W--_O6TFAH-ae7BYXsE0kncYgA6gF9AmkXuA77w_Wbn2YyjPCB9gDCkrlJUS6rvb3UJYcIBZ7W-WZlRAsRE0l6EV74c5xnyL9c7cpGMfQ-HfPsYOG4JCsrvtpAHdo7jHWTVgKoe67jWGQkNOYt1Ba7rCf4y-fqQ3d6hZoptAAcJd26yigvV4768GHQGrBvgAc7OzutOGzYARAgStpjQMp0kMiOGIzq-TUsDlvtMrx2fH8gfy2uG2HvzsROkbNedL-iO5PmswNrDvCYEWZmVjMcaVg--ZF0sjg";

        // Construct Http request headers and body per HSBC OAuth API documentation
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("x-fapi-financial-id", "test");
        headers.setCacheControl(CacheControl.noCache());

        String body = "grant_type=" + grant_type +
                "&scope=" + scope +
                "&client_assertion_type=" + client_assertion_type +
                "&client_assertion=" + client_assertion;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<HsbcAccessToken> response =
                restTemplate.exchange(access_token_url, HttpMethod.POST, request, HsbcAccessToken.class);

        logger.debug("Access Token Response ---------" + response.getBody());

        return response.getBody();
    }

    // curl -v -X POST --cert qwac.der --cert-type DER --key server.key
    // -H "Content-Type: application/json"
    // -H "Accept: application/json"
    // -H "Authorization: Bearer ea053fa4-28ad-49b4-928c-589ad73d4a03"  [consent access token]
    // -H "x-fapi-financial-id: test"
    // -H "Cache-Control: no-cache"
    // -d '{"Data":
    //       {"Permissions":
    //         ["ReadAccountsDetail",
    //           "ReadBalances",
    //           "ReadBeneficiariesDetail",
    //           "ReadDirectDebits",
    //           "ReadProducts",
    //           "ReadStandingOrdersDetail",
    //           "ReadTransactionsCredits",
    //           "ReadTransactionsDebits",
    //           "ReadTransactionsDetail",
    //           "ReadScheduledPaymentsBasic",
    //           "ReadScheduledPaymentsDetail",
    //           "ReadParty",
    //           "ReadStatementsDetail",
    //           "ReadStatementsBasic"],
    //         "ExpirationDateTime":"2025-06-11T00:00:00+00:00",
    //         "TransactionFromDateTime":"1995-07-15T00:00:00+00:00",
    //         "TransactionToDateTime":"2037-12-31T23:59:59+00:00"},
    //       "Risk":{} }'
    // "https://sandbox.hsbc.com/psd2/obie/v3.1/account-access-consents"
    public HsbcConsent getConsentID(HsbcAccessToken token) {

        String account_access_consents_url = "https://sandbox.hsbc.com/psd2/obie/v3.1/account-access-consents";
        String accessToken = token.getAccessToken();

        // Construct Http request headers and body per HSBC OAuth API documentation
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);
        headers.add("x-fapi-financial-id", "test");
        headers.setCacheControl(CacheControl.noCache());

        // todo - create objects with json mappings for body text
        String body = "{\"Data\":{\"Permissions\":[\"ReadAccountsDetail\",\"ReadBalances\",\"ReadBeneficiariesDetail\",\"ReadDirectDebits\",\"ReadProducts\",\"ReadStandingOrdersDetail\",\"ReadTransactionsCredits\",\"ReadTransactionsDebits\",\"ReadTransactionsDetail\",\"ReadScheduledPaymentsBasic\",\"ReadScheduledPaymentsDetail\",\"ReadParty\",\"ReadStatementsDetail\",\"ReadStatementsBasic\"],\"ExpirationDateTime\":\"2025-06-11T00:00:00+00:00\",\"TransactionFromDateTime\":\"1995-07-15T00:00:00+00:00\",\"TransactionToDateTime\":\"2037-12-31T23:59:59+00:00\"},\"Risk\":{} }";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<HsbcConsent> response =
                restTemplate.exchange(account_access_consents_url, HttpMethod.POST, request, HsbcConsent.class);

        logger.debug("Consent ID response ---------" + response.getBody());

        return response.getBody();
    }

    // curl -v -X GET
    // -H "Cache-Control: no-cache"
    // "https://sandbox.hsbc.com/psd2/obie/v3.1/authorize
    // ?response_type=code%20id_token
    // &client_id=211e36de-64b2-479e-ae28-8a5b41a1a940
    // &redirect_uri=http://google.com  [application redirect url]
    // &scope=openid%20accounts
    // &nonce=n-0S6_WzA2Mj
    // &state=test
    // &request=eyJhbGciOiJQUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjdmYWI4MDdkLTQ5ODgtNDAxMi04ZjEwLWE3NzY1NTc4NzQ1MCJ9.eyJpc3MiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwiYXVkIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwicmVzcG9uc2VfdHlwZSI6ImNvZGUgaWRfdG9rZW4iLCJjbGllbnRfaWQiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJyZWRpcmVjdF91cmkiOiJodHRwOi8vZ29vZ2xlLmNvbSIsInNjb3BlIjoib3BlbmlkIGFjY291bnRzIiwiY2xhaW1zIjp7InVzZXJpbmZvIjp7Im9wZW5iYW5raW5nX2ludGVudF9pZCI6eyJ2YWx1ZSI6ImRlNjc4NmRlLTlhMTctNGE3OS04MTViLWZhYjg5MGJlZWU5MyIsImVzc2VudGlhbCI6dHJ1ZX19fX0.xm7Pc86lZjynBF6GXQ0CvLD1DYEOuMr21Hf3727tnHgb4v_iTyTOMdfZ0OPky0RrfPJ_QM4x1mfHuUS-4xWn5CUjA6REtMA7tHNGrHo8oQRJPRuIx3xuLodLBylMM9D6x_emh1LJXDB0GiKgJWS4QOsa56x8VDfTRqr_fuOI2T0ZVoWIOHP4pW9euem9kNf4Dh-7El-WflD7jdPVGD0ZKltBxMIjAc-vjfS4el2-MYBadxv_4E1SRtKyXX4VmihvPvyzMHzhydaOyl2nFro5inHC_Y7byY-xGih7d2-Fwboij_CI6jwvnN99HyAJRDv8qqlxFXXKqPIqchZQ309QeQ"
    // [request = JWT]
    public String getAuthorizationCode(HsbcConsent consent) {
        String authorize_url = "https://sandbox.hsbc.com/psd2/obie/v3.1/authorize";

        // Construct Http request headers and body per HSBC OAuth API documentation
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache());
        headers.set("Accept", "*/*");
        //headers.set("Host", "sandbox.hsbc.com");


        authorize_url += "?response_type=code%20id_token";
        authorize_url += "&client_id=211e36de-64b2-479e-ae28-8a5b41a1a940";
        authorize_url += "&redirect_uri=" + APP_REDIRECT_URL;
        authorize_url += "&scope=openid%20accounts";
        authorize_url += "&nonce=n-0S6_WzA2Mj";
        authorize_url += "&state=test";
        authorize_url += "&request=" + createJwt(consent);

        HttpEntity<String> request = new HttpEntity(headers);

        URI uri = null;
        try {
             uri = new URI(authorize_url);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //RestTemplate restTemplateNoSSl = new RestTemplate();
        logger.debug("Request: " + request);
        logger.debug("Uri: " + uri);
        logger.debug("URL: " + authorize_url);
//        ResponseEntity<String> response =
//            response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);

        ResponseEntity<String> response = restTemplate.exchange(authorize_url, HttpMethod.GET, request, String.class);

        logger.debug("Authorization Code response --------- " + response.toString());
        logger.debug("Authorization Code location --------- " + response.getHeaders().getLocation());

        // todo - return HsbcAuthorizationCode
        return response.getBody();
    }

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
    public String createJwt(HsbcConsent consent) {
//        String payload = "{\"iss\":\"https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2\",";
//        payload += "\"aud\":\"211e36de-64b2-479e-ae28-8a5b41a1a940\",";
//        payload += "\"response_type\":\"code id_token\",";
//        payload += "\"client_id\":\"211e36de-64b2-479e-ae28-8a5b41a1a940\",";
//        payload += "\"redirect_uri\":\"" + APP_REDIRECT_URL + "\",";
//        payload += "\"scope\":\"openid accounts\",";
//        payload += "\"claims\":";
//        payload += "{\"userinfo\":";
//        payload += "{\"openbanking_intent_id\":";
//        payload += "{\"value\":\"" + consent.getConsentID() + "\",";
//        payload += "\"essential\":true} }}}";
//
//        Key key = null;
//        try {
//            key = readPrivateKey(new File(new URI(properties.getKey())));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //Let's set the JWT Claims
//        String jws = Jwts.builder()
//                .setHeaderParam("alg", "PS256")
//                .setHeaderParam("typ", "JWT")
//                .setHeaderParam("kid", "7fab807d-4988-4012-8f10-a77655787450")
//                .setSubject(payload)
//                .signWith(key)
//                .compact();
//
//        logger.debug("JWT - " + jws);
//        return jws;

        return "eyJhbGciOiJQUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjdmYWI4MDdkLTQ5ODgtNDAxMi04ZjEwLWE3NzY1NTc4NzQ1MCJ9.eyJpc3MiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwiYXVkIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwicmVzcG9uc2VfdHlwZSI6ImNvZGUgaWRfdG9rZW4iLCJjbGllbnRfaWQiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJyZWRpcmVjdF91cmkiOiJodHRwOi8vZ29vZ2xlLmNvbSIsInNjb3BlIjoib3BlbmlkIGFjY291bnRzIiwiY2xhaW1zIjp7InVzZXJpbmZvIjp7Im9wZW5iYW5raW5nX2ludGVudF9pZCI6eyJ2YWx1ZSI6IjM2MGExYmUyLTg3NzMtNDVhNy1hOWIxLWY3YzBlOTk5MWZlMCIsImVzc2VudGlhbCI6dHJ1ZX19fX0.eRldYeaEy7McZnsc1OqHwxKJW2olKftLt2f71WrO3hzoiV0OsAjfJZ0V9EEH7uguY5FpeOx6OknumJAk31k87aOY-BRfU-sulF4ZVYm7GNi1yReexPDLXWnIiiAziH1L_X8aQF-dQBDfZw_Mm9JB6K1caDHhG1gyVR2S_QHcsdcOQ56YWlZndBSM9xk8_ETK7MFbf5HJnRPKxnIlQxvst1LEAM9OQr4UdN-4GjtwIc7CJl1qaj_cfAvhk5M5L9Ei40QS2_I5QU5r1CScp3xL_mHyqrf4Y9jxofyeIJvx3SppNwxhyzGELylvBJCJt0wq4_Ui0ZC19-qbcNIpcNPMcw";
    }

    public RSAPrivateKey readPrivateKey(File file) throws Exception {
        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        KeyFactory factory = KeyFactory.getInstance("RSA");

        try (FileReader keyReader = new FileReader(file);
             PemReader pemReader = new PemReader(keyReader)) {

            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
            return (RSAPrivateKey) factory.generatePrivate(privKeySpec);
        }
    }

//    public RSAPrivateKey readPrivateKey(File file) throws Exception {
//        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
//
//        String privateKeyPEM = key
//                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
//                .replaceAll(System.lineSeparator(), "")
//                .replace("-----END RSA PRIVATE KEY-----", "");
//
//        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
//
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
//    }

}