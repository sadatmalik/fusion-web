package com.sadatmalik.fusionweb.services;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HsbcServiceTest {

    private HsbcAccessToken accessToken;
    private HsbcConsent consentID;
    //private HsbcAuthorizationCode authorizationCode;
    private String authorizationCode;

    @Autowired
    private HsbcService service;

    @Test
    void testGetAccessToken() {
        accessToken = service.getAccessToken();
    }

    @Test
    void testGetConsentId() {
        accessToken = service.getAccessToken();
        consentID = service.getConsentID(accessToken);
    }

    @Test
    void testCreateJwt() {
        HsbcConsent consent = new HsbcConsent();
        consent.data = new HsbcConsentData();
        consent.data.consentId = "360a1be2-8773-45a7-a9b1-f7c0e9991fe0";

        String jwt = service.createJwt(consent);
        System.out.println(jwt);

        // test against value generated at jwt.io
        assertThat(jwt).isEqualTo("eyJhbGciOiJQUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjdmYWI4MDdkLTQ5ODgtNDAxMi04ZjEwLWE3NzY1NTc4NzQ1MCJ9.eyJpc3MiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwiYXVkIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwicmVzcG9uc2VfdHlwZSI6ImNvZGUgaWRfdG9rZW4iLCJjbGllbnRfaWQiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJyZWRpcmVjdF91cmkiOiJodHRwOi8vZ29vZ2xlLmNvbSIsInNjb3BlIjoib3BlbmlkIGFjY291bnRzIiwiY2xhaW1zIjp7InVzZXJpbmZvIjp7Im9wZW5iYW5raW5nX2ludGVudF9pZCI6eyJ2YWx1ZSI6IjM2MGExYmUyLTg3NzMtNDVhNy1hOWIxLWY3YzBlOTk5MWZlMCIsImVzc2VudGlhbCI6dHJ1ZX19fX0.eRldYeaEy7McZnsc1OqHwxKJW2olKftLt2f71WrO3hzoiV0OsAjfJZ0V9EEH7uguY5FpeOx6OknumJAk31k87aOY-BRfU-sulF4ZVYm7GNi1yReexPDLXWnIiiAziH1L_X8aQF-dQBDfZw_Mm9JB6K1caDHhG1gyVR2S_QHcsdcOQ56YWlZndBSM9xk8_ETK7MFbf5HJnRPKxnIlQxvst1LEAM9OQr4UdN-4GjtwIc7CJl1qaj_cfAvhk5M5L9Ei40QS2_I5QU5r1CScp3xL_mHyqrf4Y9jxofyeIJvx3SppNwxhyzGELylvBJCJt0wq4_Ui0ZC19-qbcNIpcNPMcw");
    }

    @Test
    void testGetAuthCode() {
        //accessToken = service.getAccessToken();
        //consentID = service.getConsentID(accessToken);
        HsbcConsent consent = new HsbcConsent();
        consent.data = new HsbcConsentData();
        consent.data.consentId = "360a1be2-8773-45a7-a9b1-f7c0e9991fe0";

        authorizationCode = service.getAuthorizationCode(consent);
    }

    @Test
    void testGetAuthCodeLocal() {
        try {
            URL url = new URL("https://sandbox.hsbc.com/psd2/obie/v3.1/authorize?response_type=code%20id_token&client_id=211e36de-64b2-479e-ae28-8a5b41a1a940&redirect_uri=http://google.com&scope=openid%20accounts&nonce=n-0S6_WzA2Mj&state=test&request=eyJhbGciOiJQUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjdmYWI4MDdkLTQ5ODgtNDAxMi04ZjEwLWE3NzY1NTc4NzQ1MCJ9.eyJpc3MiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwiYXVkIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwicmVzcG9uc2VfdHlwZSI6ImNvZGUgaWRfdG9rZW4iLCJjbGllbnRfaWQiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJyZWRpcmVjdF91cmkiOiJodHRwOi8vZ29vZ2xlLmNvbSIsInNjb3BlIjoib3BlbmlkIGFjY291bnRzIiwiY2xhaW1zIjp7InVzZXJpbmZvIjp7Im9wZW5iYW5raW5nX2ludGVudF9pZCI6eyJ2YWx1ZSI6IjM2MGExYmUyLTg3NzMtNDVhNy1hOWIxLWY3YzBlOTk5MWZlMCIsImVzc2VudGlhbCI6dHJ1ZX19fX0.eRldYeaEy7McZnsc1OqHwxKJW2olKftLt2f71WrO3hzoiV0OsAjfJZ0V9EEH7uguY5FpeOx6OknumJAk31k87aOY-BRfU-sulF4ZVYm7GNi1yReexPDLXWnIiiAziH1L_X8aQF-dQBDfZw_Mm9JB6K1caDHhG1gyVR2S_QHcsdcOQ56YWlZndBSM9xk8_ETK7MFbf5HJnRPKxnIlQxvst1LEAM9OQr4UdN-4GjtwIc7CJl1qaj_cfAvhk5M5L9Ei40QS2_I5QU5r1CScp3xL_mHyqrf4Y9jxofyeIJvx3SppNwxhyzGELylvBJCJt0wq4_Ui0ZC19-qbcNIpcNPMcw");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestProperty("Cache-Control", "no-cache");

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            http.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}