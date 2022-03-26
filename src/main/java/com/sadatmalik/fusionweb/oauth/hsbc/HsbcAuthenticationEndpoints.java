package com.sadatmalik.fusionweb.oauth.hsbc;

/**
 * Intended as a Hsbc Open Banking Api interface.
 *
 * Currently, it serves as a place to encapsulate the Api endpoints. It will be
 * refactored to capture generic methods in a future revision.
 *
 * @author sadatmalik
 */
public interface HsbcAuthenticationEndpoints {

    String ACCESS_TOKEN_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2";

    String ACCOUNT_ACCESS_CONSENTS_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/account-access-consents";

    String AUTHORIZE_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/authorize";
}
