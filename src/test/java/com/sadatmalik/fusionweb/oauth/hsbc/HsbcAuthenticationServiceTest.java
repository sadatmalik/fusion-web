package com.sadatmalik.fusionweb.oauth.hsbc;

import com.nimbusds.jose.JWSObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class HsbcAuthenticationServiceTest {

    private HsbcClientAccessToken accessToken;
    private HsbcConsent consent;
    private String authorizationURL;

    @Autowired
    private HsbcAuthenticationService service;

    @Autowired
    private JwtHelper jwtHelper;

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