package com.sadatmalik.fusionweb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rest.ssl")
@Data
public class SecureRestTemplateProperties {
    //pkcs12 file
    String trustStore;
    char[] trustStorePassword;
    String protocol = "TLSv1.2";
}
