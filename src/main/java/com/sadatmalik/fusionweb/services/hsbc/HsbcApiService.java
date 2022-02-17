package com.sadatmalik.fusionweb.services.hsbc;

import com.sadatmalik.fusionweb.oauth.hsbc.HsbcUserAccessToken;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.Account;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.AccountList;
import com.sadatmalik.fusionweb.services.hsbc.model.balances.Balance;
import com.sadatmalik.fusionweb.services.hsbc.model.balances.HsbcBalanceObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HsbcApiService {

    private static final Logger logger = LoggerFactory.getLogger(HsbcApiService.class);

    private static final String ACCOUNT_INFO_URL = "https://sandbox.hsbc.com/psd2/obie/v3.1/accounts";

    @Autowired
    private RestTemplate restTemplate;

    // curl -v -X GET --cert qwac.der --cert-type DER --key server.key
    // -H "x-fapi-financial-id: test"
    // -H "Authorization: Bearer 4501825f-096a-4959-bde7-dff93a5fe6ba"
    // -H "Cache-Control: no-cache"
    // "https://sandbox.hsbc.com/psd2/obie/v3.1/accounts"
    public List<Account> getUserAccounts(HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<AccountList> response =
                restTemplate.exchange(ACCOUNT_INFO_URL, HttpMethod.GET, request, AccountList.class);

        // sets account balances with a rest call per account
        for (Account account : response.getBody().getData().getAccounts()) {
            account.setBalance(getAccountBalance(account, accessToken));
        }

        logger.debug("User Accounts ---------" + response.getBody());

        return response.getBody().getData().getAccounts();
    }

    private HttpHeaders getGetHeaders(HsbcUserAccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-fapi-financial-id", "test");
        headers.setBearerAuth(accessToken.getAccessToken());
        headers.setCacheControl(CacheControl.noCache());

        return headers;
    }

    // "GET /accounts/{AccountId}"
    public void getAccountDetails(Account account, HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(ACCOUNT_INFO_URL + "/" + account.getAccountId(),
                        HttpMethod.GET, request, String.class);

        logger.debug("User Accounts for AccountID ---------" + response.getBody());
    }

    // "GET /accounts/{AccountId}/balances"
    public Balance getAccountBalance(Account account, HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<HsbcBalanceObject> response =
                restTemplate.exchange(ACCOUNT_INFO_URL + "/" + account.getAccountId() + "/balances",
                        HttpMethod.GET, request, HsbcBalanceObject.class);

        logger.debug("Balance for AccountID ---------" + response.getBody());

        Balance balance = new Balance(
                response.getBody().getData().getBalanceList().get(0).getAmount().getAmount(),
                response.getBody().getData().getBalanceList().get(0).getCreditDebit().equals("Credit"),
                response.getBody().getData().getBalanceList().get(0).getAmount().getCurrency());

        return balance;
    }

    // "GET /accounts/{AccountId}/transactions"
    public void getTransactions(String accountId, HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(ACCOUNT_INFO_URL + "/" + accountId + "/transactions",
                        HttpMethod.GET, request, String.class);

        logger.debug("Transactions for AccountID ---------" + response.getBody());
    }
}
