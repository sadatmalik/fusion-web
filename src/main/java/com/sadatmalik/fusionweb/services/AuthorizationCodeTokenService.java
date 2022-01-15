package com.sadatmalik.fusionweb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorizationCodeTokenService {
    @Autowired
    private AuthorizationCodeConfiguration configuration;

    public String getAuthorizationEndpoint() {
        String endpoint = "https://sandbox.hsbc.com/psd2/obie/v3.1/authorize";
        Map<String, String> authParameters = new HashMap<>();
        authParameters.put("response_type", "code%20id_token");
        authParameters.put("client_id", "211e36de-64b2-479e-ae28-8a5b41a1a940");
        authParameters.put("redirect_uri",
                getEncodedUrl("http://google.com"));
        authParameters.put("scope", "openid%20accounts");
        authParameters.put("nonce", "n-0S6_WzA2Mj");
        authParameters.put("state", "test");
        authParameters.put("request", "eyJhbGciOiJQUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjdmYWI4MDdkLTQ5ODgtNDAxMi04ZjEwLWE3NzY1NTc4NzQ1MCJ9.eyJpc3MiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwiYXVkIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwicmVzcG9uc2VfdHlwZSI6ImNvZGUgaWRfdG9rZW4iLCJjbGllbnRfaWQiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJyZWRpcmVjdF91cmkiOiJodHRwOi8vZ29vZ2xlLmNvbSIsInNjb3BlIjoib3BlbmlkIGFjY291bnRzIiwiY2xhaW1zIjp7InVzZXJpbmZvIjp7Im9wZW5iYW5raW5nX2ludGVudF9pZCI6eyJ2YWx1ZSI6IjM2MGExYmUyLTg3NzMtNDVhNy1hOWIxLWY3YzBlOTk5MWZlMCIsImVzc2VudGlhbCI6dHJ1ZX19fX0.eRldYeaEy7McZnsc1OqHwxKJW2olKftLt2f71WrO3hzoiV0OsAjfJZ0V9EEH7uguY5FpeOx6OknumJAk31k87aOY-BRfU-sulF4ZVYm7GNi1yReexPDLXWnIiiAziH1L_X8aQF-dQBDfZw_Mm9JB6K1caDHhG1gyVR2S_QHcsdcOQ56YWlZndBSM9xk8_ETK7MFbf5HJnRPKxnIlQxvst1LEAM9OQr4UdN-4GjtwIc7CJl1qaj_cfAvhk5M5L9Ei40QS2_I5QU5r1CScp3xL_mHyqrf4Y9jxofyeIJvx3SppNwxhyzGELylvBJCJt0wq4_Ui0ZC19-qbcNIpcNPMcw");
        return buildUrl(endpoint, authParameters);
    }

    private String buildUrl(String endpoint, Map<String, String> parameters) {
        List<String> paramList = new ArrayList<>(parameters.size());
        parameters.forEach((name, value) -> {
            paramList.add(name + "=" + value);
        });
        return endpoint + "?" + paramList.stream()
                .reduce((a, b) -> a + "&" + b).get();
    }

    private String getEncodedUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    // @todo
    public OAuth2Token getToken(String authorizationCode) {
        RestTemplate rest = new RestTemplate();
        String authBase64 = configuration.encodeCredentials(
                "clientapp", "123456");
        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(
                configuration.getBody(authorizationCode),
                configuration.getHeader(authBase64), HttpMethod.POST,
                URI.create("http://localhost:8080/oauth/token"));
        ResponseEntity<OAuth2Token> responseEntity = rest.exchange(
                requestEntity, OAuth2Token.class);
        if (responseEntity.getStatusCode().is2xxSuccessful())
            return responseEntity.getBody();
        throw new RuntimeException("error trying to retrieve access token");
    }
}