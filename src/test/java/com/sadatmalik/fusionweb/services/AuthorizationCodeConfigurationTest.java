package com.sadatmalik.fusionweb.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorizationCodeConfigurationTest {

    @Autowired
    private AuthorizationCodeConfiguration configuration;

    @Test
    void encodeCredentials() {
        String encoded = configuration.encodeCredentials("Sadat", "password");
        System.out.println(encoded);
    }

    @Test
    void getBody() {
    }

    @Test
    void getHeader() {
    }
}