package com.sadatmalik.fusionweb.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class OauthConfigTest {

    @Autowired
    OauthConfig oauthConfig;

    @Autowired
    Environment env;

    @Test
    void getAppRedirectUrl() {
        assertNotNull(oauthConfig.getAppRedirectUrl());

        assertEquals(env.getProperty("creativefusion.net.app-redirect-url"),
                oauthConfig.getAppRedirectUrl());
    }
}