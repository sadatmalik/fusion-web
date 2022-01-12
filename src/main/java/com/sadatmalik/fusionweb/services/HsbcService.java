package com.sadatmalik.fusionweb.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class HsbcService {

    private static final Logger logger = LoggerFactory.getLogger(HsbcService.class);

    @Autowired
    RestTemplate restTemplate;

    // curl -v -X POST --cert qwac.der --cert-type DER --key server.key
    // -H "Content-Type: application/x-www-form-urlencoded"
    // -H "Accept: application/json"
    // -H "x-fapi-financial-id: test"
    // -H "Cache-Control: no-cache"
    // -d 'grant_type=client_credentials
    // &scope=accounts
    // &client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer
    // &client_assertion=eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkZThjYTc3LWQ2ODEtNDc4Mi04MTIyLWUwMzkyNTg5MDIxYiJ9.eyJpc3MiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJhdWQiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwic3ViIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwiaWF0IjoxNDk5MTgzNjAxLCJleHAiOjE3NzkzNDg1MjF9.uu282OmEHUa0t6z6T68MfXzEGGgq8PiWuyJxuNQ1be6iWdD5sVbw3W--_O6TFAH-ae7BYXsE0kncYgA6gF9AmkXuA77w_Wbn2YyjPCB9gDCkrlJUS6rvb3UJYcIBZ7W-WZlRAsRE0l6EV74c5xnyL9c7cpGMfQ-HfPsYOG4JCsrvtpAHdo7jHWTVgKoe67jWGQkNOYt1Ba7rCf4y-fqQ3d6hZoptAAcJd26yigvV4768GHQGrBvgAc7OzutOGzYARAgStpjQMp0kMiOGIzq-TUsDlvtMrx2fH8gfy2uG2HvzsROkbNedL-iO5PmswNrDvCYEWZmVjMcaVg--ZF0sjg'
    // "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2"
    public void getAccessToken() {

        String access_token_url = "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2";

        String grant_type = "client_credentials";
        String scope = "accounts";
        String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
        String client_assertion = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkZThjYTc3LWQ2ODEtNDc4Mi04MTIyLWUwMzkyNTg5MDIxYiJ9.eyJpc3MiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJhdWQiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwic3ViIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwiaWF0IjoxNDk5MTgzNjAxLCJleHAiOjE3NzkzNDg1MjF9.uu282OmEHUa0t6z6T68MfXzEGGgq8PiWuyJxuNQ1be6iWdD5sVbw3W--_O6TFAH-ae7BYXsE0kncYgA6gF9AmkXuA77w_Wbn2YyjPCB9gDCkrlJUS6rvb3UJYcIBZ7W-WZlRAsRE0l6EV74c5xnyL9c7cpGMfQ-HfPsYOG4JCsrvtpAHdo7jHWTVgKoe67jWGQkNOYt1Ba7rCf4y-fqQ3d6hZoptAAcJd26yigvV4768GHQGrBvgAc7OzutOGzYARAgStpjQMp0kMiOGIzq-TUsDlvtMrx2fH8gfy2uG2HvzsROkbNedL-iO5PmswNrDvCYEWZmVjMcaVg--ZF0sjg";

        // Construct Http request headers and body per HSBC OAuth API documentation
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("x-fapi-financial-id", "test");
        headers.setCacheControl(CacheControl.noCache());

        String body = "grant_type=" + grant_type +
                "&scope=" + scope +
                "&client_assertion_type=" + client_assertion_type +
                "&client_assertion=" + client_assertion;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<HsbcAccessToken> response =
                restTemplate.exchange(access_token_url, HttpMethod.POST, request, HsbcAccessToken.class);

        logger.debug("Access Token Response ---------" + response.getBody());
    }

}