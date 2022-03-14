package com.sadatmalik.fusionweb.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = OauthConfig.class)
@TestPropertySource("classpath:application-${spring.profiles.active}.properties")
//@ConfigurationProperties("creativefusion.net")
class OauthConfigTest {

    @Autowired
    OauthConfig oauthConfig;

    @Autowired
    Environment env;

    @Test
    void getAppRedirectUrl() {
        System.out.println(env.getProperty("creativefusion.net.app-redirect-url"));
        assertEquals(env.getProperty("creativefusion.net.app-redirect-url"),
                oauthConfig.getAppRedirectUrl());
    }
}