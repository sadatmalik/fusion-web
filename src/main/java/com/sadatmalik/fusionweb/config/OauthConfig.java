package com.sadatmalik.fusionweb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

/**
 * Loads configuration from properties files. It will read all properties
 * prefixed with "creativefusion.net"
 *
 * For example the appRedirectUrl property will be read from the property
 * named: {@code creativefusion.net.app-redirect-url}  - note the use of
 * hyphenation.
 *
 * Additional properties related to the core application can be easily
 * maintained here as needed.
 *
 * Note the {@code @Configuration} annotation triggers a configuration
 * properties scan for all classes in this package annotated with
 * {@code @ConfigurationProperties}.
 *
 * @author sadatmalik
 */
@Configuration
@ConfigurationProperties("creativefusion.net")
@Data
public class OauthConfig {

    @NotBlank
    private String appRedirectUrl;

}
