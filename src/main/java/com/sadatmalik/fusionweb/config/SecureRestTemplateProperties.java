package com.sadatmalik.fusionweb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Loads configuration from properties files. It will read all properties
 * prefixed with "rest.ssl"
 *
 * For example the trustStorePassword property will be read from the property
 * named: {@code rest.ssl.trust-store-password} - note the use of hyphenation.
 *
 * Additional properties related to RestTemplate and SSL configuration can be
 * easily maintained here as needed.
 *
 * @author sadatmalik
 */
@ConfigurationProperties("rest.ssl")
@Data
public class SecureRestTemplateProperties {
    //points to pkcs12 file
    String trustStore;
    char[] trustStorePassword;
    String protocol = "TLSv1.2";
}
