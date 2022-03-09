package com.sadatmalik.fusionweb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "creativefusion.net")
public class OauthConfig {

    @NotBlank
    private String appRedirectUrl;

}
