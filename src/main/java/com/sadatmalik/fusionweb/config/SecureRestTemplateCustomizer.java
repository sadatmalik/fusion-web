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

/**
 * This class implement RestTemplateCustomizer. Spring invokes the customize method
 * on the RestTemplate argument.
 *
 * Essentially this class is responsible for customizing the rest template that will
 * be used for connecting to the OB API - in this case for HSBC.
 *
 * It uses the SecureRestTemplateProperties for property information required to
 * properly customize the rest template. For instance the required trust store and
 * password are managed via external configuration files.
 *
 * @see #customize(RestTemplate)
 * @author sadatmalik
 */
@Component
@EnableConfigurationProperties(SecureRestTemplateProperties.class)
@RequiredArgsConstructor
@Slf4j
public class SecureRestTemplateCustomizer implements RestTemplateCustomizer {

    private final SecureRestTemplateProperties properties;

    /**
     * Will return a RestTemplate bean throughout the application. Spring
     * will ensure the template is properly customized with SSL security before
     * it is used in the application, by calling the customize method.
     *
     * @param builder the Spring context provides this
     * @return
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    /**
     * This is the method implementation of the RestTemplateCustomizer interface.
     *
     * An SSLContext is created using the trust store details from the application
     * properties. An HttpClient is then created using this SSLContext.
     *
     * Finally, a ClientHttpRequestFactory is initialized with the HttpClient and
     * SSLContext - this is then set as the HTTP request factory for the rest template.
     *
     * Every time the rest template is subsequently used, it will use the factory to
     * generate HTTP requests using the HTTP client and SSL context passed to the
     * factory.
     *
     * Future releases will likely need one of these per connected organisation - i.e.
     * will need to have one of these per connected bank api.
     *
     * @param restTemplate injected from the Spring context
     */
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
