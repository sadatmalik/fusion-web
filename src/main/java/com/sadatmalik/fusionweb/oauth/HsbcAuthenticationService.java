package com.sadatmalik.fusionweb.oauth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class HsbcAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(HsbcAuthenticationService.class);
    static final String APP_REDIRECT_URL = "http://localhost:8080";
    static final String USER_REDIRECT_URL = "http://localhost:8080/dashboard";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JwtHelper jwtHelper;

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
    public HsbcClientAccessToken getAccessToken() {

        String access_token_url = "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2";

        String grant_type = "client_credentials";
        String scope = "accounts";
        String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
        String client_assertion = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkZThjYTc3LWQ2ODEtNDc4Mi04MTIyLWUwMzkyNTg5MDIxYiJ9.eyJpc3MiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJhdWQiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwic3ViIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwiaWF0IjoxNDk5MTgzNjAxLCJleHAiOjE3NzkzNDg1MjF9.uu282OmEHUa0t6z6T68MfXzEGGgq8PiWuyJxuNQ1be6iWdD5sVbw3W--_O6TFAH-ae7BYXsE0kncYgA6gF9AmkXuA77w_Wbn2YyjPCB9gDCkrlJUS6rvb3UJYcIBZ7W-WZlRAsRE0l6EV74c5xnyL9c7cpGMfQ-HfPsYOG4JCsrvtpAHdo7jHWTVgKoe67jWGQkNOYt1Ba7rCf4y-fqQ3d6hZoptAAcJd26yigvV4768GHQGrBvgAc7OzutOGzYARAgStpjQMp0kMiOGIzq-TUsDlvtMrx2fH8gfy2uG2HvzsROkbNedL-iO5PmswNrDvCYEWZmVjMcaVg--ZF0sjg";

        // Construct Http request headers and body per HSBC OAuth API documentation
        HttpHeaders headers = getStandardPostHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String body = "grant_type=" + grant_type +
                "&scope=" + scope +
                "&client_assertion_type=" + client_assertion_type +
                "&client_assertion=" + client_assertion;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<HsbcClientAccessToken> response =
                restTemplate.exchange(access_token_url, HttpMethod.POST, request, HsbcClientAccessToken.class);

        logger.debug("Access Token Response ---------" + response.getBody());

        return response.getBody();
    }

    // curl -v -X POST --cert qwac.der --cert-type DER --key server.key
    // -H "Content-Type: application/json"
    // -H "Accept: application/json"
    // -H "Authorization: Bearer ea053fa4-28ad-49b4-928c-589ad73d4a03"  [consent access token]
    // -H "x-fapi-financial-id: test"
    // -H "Cache-Control: no-cache"
    // -d '{"Data":
    //       {"Permissions":
    //         ["ReadAccountsDetail",
    //           "ReadBalances",
    //           "ReadBeneficiariesDetail",
    //           "ReadDirectDebits",
    //           "ReadProducts",
    //           "ReadStandingOrdersDetail",
    //           "ReadTransactionsCredits",
    //           "ReadTransactionsDebits",
    //           "ReadTransactionsDetail",
    //           "ReadScheduledPaymentsBasic",
    //           "ReadScheduledPaymentsDetail",
    //           "ReadParty",
    //           "ReadStatementsDetail",
    //           "ReadStatementsBasic"],
    //         "ExpirationDateTime":"2025-06-11T00:00:00+00:00",
    //         "TransactionFromDateTime":"1995-07-15T00:00:00+00:00",
    //         "TransactionToDateTime":"2037-12-31T23:59:59+00:00"},
    //       "Risk":{} }'
    // "https://sandbox.hsbc.com/psd2/obie/v3.1/account-access-consents"
    public HsbcConsent getConsentID(HsbcClientAccessToken token) {

        String account_access_consents_url = "https://sandbox.hsbc.com/psd2/obie/v3.1/account-access-consents";
        String accessToken = token.getAccessToken();

        // Construct Http request headers and body per HSBC OAuth API documentation
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);
        headers.add("x-fapi-financial-id", "test");
        headers.setCacheControl(CacheControl.noCache());

        // todo - create objects with json mappings for body text
        String body = "{\"Data\":{\"Permissions\":[\"ReadAccountsDetail\",\"ReadBalances\",\"ReadBeneficiariesDetail\",\"ReadDirectDebits\",\"ReadProducts\",\"ReadStandingOrdersDetail\",\"ReadTransactionsCredits\",\"ReadTransactionsDebits\",\"ReadTransactionsDetail\",\"ReadScheduledPaymentsBasic\",\"ReadScheduledPaymentsDetail\",\"ReadParty\",\"ReadStatementsDetail\",\"ReadStatementsBasic\"],\"ExpirationDateTime\":\"2025-06-11T00:00:00+00:00\",\"TransactionFromDateTime\":\"1995-07-15T00:00:00+00:00\",\"TransactionToDateTime\":\"2037-12-31T23:59:59+00:00\"},\"Risk\":{} }";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<HsbcConsent> response =
                restTemplate.exchange(account_access_consents_url, HttpMethod.POST, request, HsbcConsent.class);

        logger.debug("Consent ID response ---------" + response.getBody());

        return response.getBody();
    }

    // curl -v -X GET
    // -H "Cache-Control: no-cache"
    // "https://sandbox.hsbc.com/psd2/obie/v3.1/authorize
    // ?response_type=code%20id_token
    // &client_id=211e36de-64b2-479e-ae28-8a5b41a1a940
    // &redirect_uri=http://google.com  [application redirect url]
    // &scope=openid%20accounts
    // &nonce=n-0S6_WzA2Mj
    // &state=test
    // &request=eyJhbGciOiJQUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjdmYWI4MDdkLTQ5ODgtNDAxMi04ZjEwLWE3NzY1NTc4NzQ1MCJ9.eyJpc3MiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwiYXVkIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwicmVzcG9uc2VfdHlwZSI6ImNvZGUgaWRfdG9rZW4iLCJjbGllbnRfaWQiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJyZWRpcmVjdF91cmkiOiJodHRwOi8vZ29vZ2xlLmNvbSIsInNjb3BlIjoib3BlbmlkIGFjY291bnRzIiwiY2xhaW1zIjp7InVzZXJpbmZvIjp7Im9wZW5iYW5raW5nX2ludGVudF9pZCI6eyJ2YWx1ZSI6ImRlNjc4NmRlLTlhMTctNGE3OS04MTViLWZhYjg5MGJlZWU5MyIsImVzc2VudGlhbCI6dHJ1ZX19fX0.xm7Pc86lZjynBF6GXQ0CvLD1DYEOuMr21Hf3727tnHgb4v_iTyTOMdfZ0OPky0RrfPJ_QM4x1mfHuUS-4xWn5CUjA6REtMA7tHNGrHo8oQRJPRuIx3xuLodLBylMM9D6x_emh1LJXDB0GiKgJWS4QOsa56x8VDfTRqr_fuOI2T0ZVoWIOHP4pW9euem9kNf4Dh-7El-WflD7jdPVGD0ZKltBxMIjAc-vjfS4el2-MYBadxv_4E1SRtKyXX4VmihvPvyzMHzhydaOyl2nFro5inHC_Y7byY-xGih7d2-Fwboij_CI6jwvnN99HyAJRDv8qqlxFXXKqPIqchZQ309QeQ"
    // [request = JWT]
    public String getAuthorizationURL(HsbcConsent consent) {
        String authorize_url = "https://sandbox.hsbc.com/psd2/obie/v3.1/authorize";

        authorize_url += "?response_type=code%20id_token";
        authorize_url += "&client_id=211e36de-64b2-479e-ae28-8a5b41a1a940";
        authorize_url += "&redirect_uri=" + APP_REDIRECT_URL;
        authorize_url += "&scope=openid%20accounts";
        authorize_url += "&nonce=n-0S6_WzA2Mj";
        authorize_url += "&state=test";
        authorize_url += "&request=" + jwtHelper.createJwt(consent).serialize();

        return authorize_url;
    }

    // -H "Content-Type: application/x-www-form-urlencoded"
    // -H "x-fapi-financial-id: test"
    // -H "Cache-Control: no-cache"
    private HttpHeaders getStandardPostHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("x-fapi-financial-id", "test");
        headers.setCacheControl(CacheControl.noCache());

        return headers;
    }

    //curl -v -X POST --cert qwac.der --cert-type DER --key server.key
    // -H "Content-Type: application/x-www-form-urlencoded"
    // -H "x-fapi-financial-id: test"
    // -H "Cache-Control: no-cache"
    // -d 'grant_type=authorization_code
    //     &code=93349a61-5e4b-4af0-bc09-867b3e73d21a
    //     &redirect_uri=http://google.com
    //     &client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer
    //     &client_assertion=eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkZThjYTc3LWQ2ODEtNDc4Mi04MTIyLWUwMzkyNTg5MDIxYiJ9.eyJpc3MiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJhdWQiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwic3ViIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwiaWF0IjoxNDk5MTgzNjAxLCJleHAiOjE3NzkzNDg1MjF9.uu282OmEHUa0t6z6T68MfXzEGGgq8PiWuyJxuNQ1be6iWdD5sVbw3W--_O6TFAH-ae7BYXsE0kncYgA6gF9AmkXuA77w_Wbn2YyjPCB9gDCkrlJUS6rvb3UJYcIBZ7W-WZlRAsRE0l6EV74c5xnyL9c7cpGMfQ-HfPsYOG4JCsrvtpAHdo7jHWTVgKoe67jWGQkNOYt1Ba7rCf4y-fqQ3d6hZoptAAcJd26yigvV4768GHQGrBvgAc7OzutOGzYARAgStpjQMp0kMiOGIzq-TUsDlvtMrx2fH8gfy2uG2HvzsROkbNedL-iO5PmswNrDvCYEWZmVjMcaVg--ZF0sjg'
    //     "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2"
    public HsbcUserAccessToken getAccessToken(String authCode) {
        HttpHeaders headers = getStandardPostHeaders();

        String grant_type = "authorization_code";
        String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
        String client_assertion = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkZThjYTc3LWQ2ODEtNDc4Mi04MTIyLWUwMzkyNTg5MDIxYiJ9.eyJpc3MiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJhdWQiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwic3ViIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwiaWF0IjoxNDk5MTgzNjAxLCJleHAiOjE3NzkzNDg1MjF9.uu282OmEHUa0t6z6T68MfXzEGGgq8PiWuyJxuNQ1be6iWdD5sVbw3W--_O6TFAH-ae7BYXsE0kncYgA6gF9AmkXuA77w_Wbn2YyjPCB9gDCkrlJUS6rvb3UJYcIBZ7W-WZlRAsRE0l6EV74c5xnyL9c7cpGMfQ-HfPsYOG4JCsrvtpAHdo7jHWTVgKoe67jWGQkNOYt1Ba7rCf4y-fqQ3d6hZoptAAcJd26yigvV4768GHQGrBvgAc7OzutOGzYARAgStpjQMp0kMiOGIzq-TUsDlvtMrx2fH8gfy2uG2HvzsROkbNedL-iO5PmswNrDvCYEWZmVjMcaVg--ZF0sjg";

        String access_token_url = "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2";

        String body = "grant_type=" + grant_type +
                "&code=" + authCode +
                "&redirect_uri=" + APP_REDIRECT_URL +
                "&client_assertion_type=" + client_assertion_type +
                "&client_assertion=" + client_assertion;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<HsbcUserAccessToken> response =
                restTemplate.exchange(access_token_url, HttpMethod.POST, request, HsbcUserAccessToken.class);

        logger.debug("User Access Token Response ---------" + response.getBody());

        return response.getBody();
    }
}