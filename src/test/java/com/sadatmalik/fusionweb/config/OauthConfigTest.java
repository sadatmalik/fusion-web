package com.sadatmalik.fusionweb.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = OauthConfig.class)
@TestPropertySource("classpath:application-dev.properties")
class OauthConfigTest {

    @Autowired
    OauthConfig oauthConfig;

    @Test
    void getAppRedirectUrl() {
        assertEquals("http://localhost:8080", oauthConfig.getAppRedirectUrl());
    }
}