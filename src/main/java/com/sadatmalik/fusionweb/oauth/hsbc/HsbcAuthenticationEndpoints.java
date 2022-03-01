package com.sadatmalik.fusionweb.oauth.hsbc;

public interface HsbcAuthenticationEndpoints {

    String APP_REDIRECT_URL = "http://localhost:8080";

    String ACCESS_TOKEN_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/as/token.oauth2";

    String ACCOUNT_ACCESS_CONSENTS_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/account-access-consents";

    String AUTHORIZE_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/authorize";
}
