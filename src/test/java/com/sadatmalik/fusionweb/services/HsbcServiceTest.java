package com.sadatmalik.fusionweb.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HsbcServiceTest {

    @Autowired
    private HsbcService service;

    @Test
    void getAccessToken() {
        service.getAccessToken();
    }

}