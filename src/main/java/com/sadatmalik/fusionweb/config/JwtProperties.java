package com.sadatmalik.fusionweb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
@Data
public class JwtProperties {
    String key;
}
