package com.sadatmalik.fusionweb.tracking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * POJO class that contains all the specific data we want to store in the UserContextHolder.
 *
 * The UserContext class holds the HTTP header values for an individual service client
 * request that is processed by our microservice.
 *
 * @author sadatmalik
 */
@Getter
@Setter
@Component
public class UserContext {
    public static final String CORRELATION_ID  = "tmx-correlation-id";
    public static final String AUTH_TOKEN      = "Authorization";

    private static final ThreadLocal<String> correlationId= new ThreadLocal<>();
    private static final ThreadLocal<String> authToken= new ThreadLocal<>();

    public static String getCorrelationId() {
        return correlationId.get();
    }

    public static void setCorrelationId(String cid) {
        correlationId.set(cid);
    }

    public static String getAuthToken() {
        return authToken.get();
    }

    public static void setAuthToken(String aToken) {
        authToken.set(aToken);
    }

    public static HttpHeaders getHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CORRELATION_ID, getCorrelationId());
        return httpHeaders;
    }
}