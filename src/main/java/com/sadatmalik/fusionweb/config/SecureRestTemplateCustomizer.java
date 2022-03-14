package com.sadatmalik.fusionweb.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.URL;
import java.util.Arrays;

@Component
@EnableConfigurationProperties(SecureRestTemplateProperties.class)
@RequiredArgsConstructor
@Slf4j
public class SecureRestTemplateCustomizer implements RestTemplateCustomizer {

    private final SecureRestTemplateProperties properties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Override
    public void customize(RestTemplate restTemplate) {

        try {
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadKeyMaterial(new URL(properties.getTrustStore()),
                            properties.getTrustStorePassword(),
                            properties.getTrustStorePassword())
                    .setProtocol(properties.getProtocol())
                    .build();

            HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLContext(sslContext)
                    .build();

            ClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory(httpClient);

            log.info("Registered SSL truststore {} for client requests",
                    properties.getTrustStore());

            restTemplate.setRequestFactory(requestFactory);

        } catch (Exception e) {

            throw new IllegalStateException("Failed to setup client SSL context", e);

        } finally {
            // zero out passwords - good security practice
            Arrays.fill(properties.getTrustStorePassword(), (char) 0);
        }
    }
}
