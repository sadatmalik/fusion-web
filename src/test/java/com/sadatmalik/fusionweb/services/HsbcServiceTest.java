package com.sadatmalik.fusionweb.services;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HsbcServiceTest {

    private HsbcAccessToken accessToken;
    private HsbcConsent consentID;

    @Autowired
    private HsbcService service;

    @Test
    @Order(1)
    void getAccessToken() {
        accessToken = service.getAccessToken();
    }

    @Test
    @Order(2)
    void getConsentId() {
        accessToken = service.getAccessToken();
        consentID = service.getConsentID(accessToken);
    }


}