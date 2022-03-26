package com.sadatmalik.fusionweb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Loads configuration from properties files. It will read all properties
 * prefixed with "jwt"
 *
 * For example the key property will be read from the property named:
 * {@code jwt.key}
 *
 * Additional properties related to JWT processing can be easily
 * maintained here as needed.
 *
 * @author sadatmalik
 */
@ConfigurationProperties("jwt")
@Data
public class JwtProperties {
    String key;
}
