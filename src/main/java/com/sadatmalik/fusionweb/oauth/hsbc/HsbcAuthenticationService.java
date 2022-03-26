package com.sadatmalik.fusionweb.oauth.hsbc;

import com.sadatmalik.fusionweb.config.OauthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * This service encapsulated the OAuth authentication flow with Hsbc's Open
 * Banking Api implementation.
 *
 * The primary methods are:
 *
 * {@code getAccessToken()} retrieves a client (application) token.
 *
 * {@code getConsentId()} uses the client token to retrieve a consent.
 *
 * {@code getAuthorizationUrl()} uses the consent to retrieve an account
 * authorisation url and subsequently an authorisation code.
 *
 * {@code getAccessToken()} uses the authorisation code to retrieve a
 * user access token. This token will be used to make all subsequent Api
 * requests related to accounts and transactions on behalf of this use - and
 * only for the accounts that the user has pre-authorised.
 *
 * @author sadatmalik
 */
@Slf4j
@Service
@RequiredArgsConstructor
public final class HsbcAuthenticationService implements HsbcAuthenticationEndpoints {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    OauthConfig oauthConfig;

    /**
     * Initiates the two-step OAuth flow - requests and if successfully
     * authenticated, retrieves an access token on behalf of the client application,
     * i.e. Fusion.
     *
     * Translates the retrieved Json to an HsbcClientAccessToken.
     *
     * @return returns a properly formatted HsbcClientAccessToken.
     */
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
                restTemplate.exchange(ACCESS_TOKEN_URL, HttpMethod.POST, request, HsbcClientAccessToken.class);

        log.debug("Access Token Response ---------" + response.getBody());

        return response.getBody();
    }

    /**
     * Uses the client access token to create a Rest Api request for OAuth consent.
     * If successful, retrieves a Json consent and converts to a properly formatted
     * HsbcConsent instance.
     *
     * @param token the client access token.
     * @return returns the consent instance.
     */
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
                restTemplate.exchange(ACCOUNT_ACCESS_CONSENTS_URL, HttpMethod.POST, request, HsbcConsent.class);

        log.debug("Consent ID response ---------" + response.getBody());

        return response.getBody();
    }

    /**
     * Uses the consent to create a properly formatted Open Banking authorisation url,
     * including a signed JWT parameter within the constructed url.
     *
     * @param consent the Api consent.
     * @return the Api authorisation Url.
     */
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

        String authorize_url = AUTHORIZE_URL + "?response_type=code%20id_token";
        authorize_url += "&client_id=211e36de-64b2-479e-ae28-8a5b41a1a940";
        authorize_url += "&redirect_uri=" + oauthConfig.getAppRedirectUrl();
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

    /**
     * By this stage an authorisation code will have been included in the far end's
     * redirection response back to Fusion's application redirect url endpoint after
     * successful negotiation of the all previous Oauth flow steps.
     *
     * This authorisation code is used to request a user access token. Upon success,
     * the retrieved Json token is converted to a properly formatted HsbcUserAccessToken.
     * All subsequent user requests to the Open Banking Api must include the token,
     * which may be refreshed upon expiry.
     *
     * @param authCode the authorisation code from the far end's redirect.
     * @return the user access token - the final return from successful negotiation
     * of the complete Oauth flow sequence.
     */
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

        String body = "grant_type=" + grant_type +
                "&code=" + authCode +
                "&redirect_uri=" + oauthConfig.getAppRedirectUrl() +
                "&client_assertion_type=" + client_assertion_type +
                "&client_assertion=" + client_assertion;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<HsbcUserAccessToken> response =
                restTemplate.exchange(ACCESS_TOKEN_URL, HttpMethod.POST, request, HsbcUserAccessToken.class);

        log.debug("User Access Token Response ---------" + response.getBody());

        return response.getBody();
    }

    /**
     * Handles the refreshing of an expired user access token. Notice that there is no
     * longer a need to go through the entire Oauth flow sequence.
     *
     * Not intended to be called directly from client classes.
     *
     * @param refreshToken the expired user access token.
     * @return returns a valid user access token.
     */
    //curl -v -X POST --cert qwac.der --cert-type DER --key server.key
    // -H "Content-Type: application/x-www-form-urlencoded"
    // -H "x-fapi-financial-id: test"
    // -H "Cache-Control: no-cache"
    // -d 'grant_type=refresh_token
    //     &refresh_token=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
    //     &redirect_uri=http://google.com
    //     &client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer
    //     &client_assertion=eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkZThjYTc3LWQ2ODEtNDc4Mi04MTIyLWUwMzkyNTg5MDIxYiJ9.eyJpc3MiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJhdWQiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwic3ViIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwiaWF0IjoxNDk5MTgzNjAxLCJleHAiOjE3NzkzNDg1MjF9.uu282OmEHUa0t6z6T68MfXzEGGgq8PiWuyJxuNQ1be6iWdD5sVbw3W--_O6TFAH-ae7BYXsE0kncYgA6gF9AmkXuA77w_Wbn2YyjPCB9gDCkrlJUS6rvb3UJYcIBZ7W-WZlRAsRE0l6EV74c5xnyL9c7cpGMfQ-HfPsYOG4JCsrvtpAHdo7jHWTVgKoe67jWGQkNOYt1Ba7rCf4y-fqQ3d6hZoptAAcJd26yigvV4768GHQGrBvgAc7OzutOGzYARAgStpjQMp0kMiOGIzq-TUsDlvtMrx2fH8gfy2uG2HvzsROkbNedL-iO5PmswNrDvCYEWZmVjMcaVg--ZF0sjg'
    //     "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2"
    HsbcUserAccessToken refreshAccessToken(HsbcUserAccessToken refreshToken) {
        HttpHeaders headers = getStandardPostHeaders();

        String grant_type = "refresh_token";
        String client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
        String client_assertion = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjhkZThjYTc3LWQ2ODEtNDc4Mi04MTIyLWUwMzkyNTg5MDIxYiJ9.eyJpc3MiOiIyMTFlMzZkZS02NGIyLTQ3OWUtYWUyOC04YTViNDFhMWE5NDAiLCJhdWQiOiJodHRwczovL3NhbmRib3guaHNiYy5jb20vcHNkMi9vYmllL3YzLjEvYXMvdG9rZW4ub2F1dGgyIiwic3ViIjoiMjExZTM2ZGUtNjRiMi00NzllLWFlMjgtOGE1YjQxYTFhOTQwIiwiaWF0IjoxNDk5MTgzNjAxLCJleHAiOjE3NzkzNDg1MjF9.uu282OmEHUa0t6z6T68MfXzEGGgq8PiWuyJxuNQ1be6iWdD5sVbw3W--_O6TFAH-ae7BYXsE0kncYgA6gF9AmkXuA77w_Wbn2YyjPCB9gDCkrlJUS6rvb3UJYcIBZ7W-WZlRAsRE0l6EV74c5xnyL9c7cpGMfQ-HfPsYOG4JCsrvtpAHdo7jHWTVgKoe67jWGQkNOYt1Ba7rCf4y-fqQ3d6hZoptAAcJd26yigvV4768GHQGrBvgAc7OzutOGzYARAgStpjQMp0kMiOGIzq-TUsDlvtMrx2fH8gfy2uG2HvzsROkbNedL-iO5PmswNrDvCYEWZmVjMcaVg--ZF0sjg";

        String body = "grant_type=" + grant_type +
                "&refresh_token=" + refreshToken.getRefreshToken() +
                "&redirect_uri=" + oauthConfig.getAppRedirectUrl() +
                "&client_assertion_type=" + client_assertion_type +
                "&client_assertion=" + client_assertion;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<HsbcUserAccessToken> response =
                restTemplate.exchange(ACCESS_TOKEN_URL, HttpMethod.POST, request, HsbcUserAccessToken.class);

        // @todo add response validations
        // @todo consider global exception handler

        log.debug("Refresh User Access Token Response ---------" + response.getBody());

        return response.getBody();
    }

    /**
     * Clients classes will call this method to both verify if a token is valid, and
     * in case it has expired, it will be internally refreshed by the call to {@code
     * refreshAccessToken()}.
     *
     * @see HsbcAuthenticationService#refreshAccessToken(HsbcUserAccessToken)
     * @param token the user access token
     */
    public void validateTokenExpiry(HsbcUserAccessToken token) {
        // @todo handle the refresh in UserDetails on a timer?
        if (token.isExpiring()) {
            HsbcUserAccessToken.setCurrentToken(refreshAccessToken(token));
        }
    }

}