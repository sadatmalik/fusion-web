package com.sadatmalik.fusionweb.services.hsbc;

import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcAuthenticationService;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcUserAccessToken;
import com.sadatmalik.fusionweb.repositories.BankRepository;
import com.sadatmalik.fusionweb.services.AccountServicesRegistry;
import com.sadatmalik.fusionweb.services.TransactionService;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.HsbcAccount;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.HsbcAccountList;
import com.sadatmalik.fusionweb.services.hsbc.model.balances.HsbcBalanceObject;
import com.sadatmalik.fusionweb.services.hsbc.model.transacations.HsbcTransaction;
import com.sadatmalik.fusionweb.services.hsbc.model.transacations.HsbcTransactionObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HsbcApiService implements HsbcOpenBankingService, TransactionService {

    private final HsbcAuthenticationService hsbcAuth;
    private final RestTemplate restTemplate;
    private final BankRepository bankRepository;
    private final AccountServicesRegistry accountServicesRegistry;

    private final Bank hsbc;

    @Autowired
    public HsbcApiService(HsbcAuthenticationService hsbcAuth,
                          RestTemplate restTemplate, AccountServicesRegistry accountServicesRegistry,
                          BankRepository bankRepository) {
        this.hsbcAuth = hsbcAuth;
        this.restTemplate = restTemplate;
        this.accountServicesRegistry = accountServicesRegistry;
        this.bankRepository = bankRepository;
        this.hsbc = bankRepository.save(Bank.builder()
                .name("HSBC")
                .imageLocation("/images/hsbc.png")
                .build()
        );
    }

    // Public methods
    public List<Account> getUserAccounts() {
        final List<Account> accounts = new ArrayList<>();

        if (HsbcUserAccessToken.current() != null) {
            hsbcAuth.validateTokenExpiry(HsbcUserAccessToken.current());
            final List<HsbcAccount> hsbcAccounts = getUserAccounts(HsbcUserAccessToken.current());

            // @todo use mapStruct mapper
            for (HsbcAccount hsbcAccount : hsbcAccounts) {
                Account account = Account.builder()
                        .accountId(hsbcAccount.getAccountId())
                        .name(hsbcAccount.getAccountInfo().get(0).getName())
                        .type(AccountType.CURRENT) // @todo take this from hsbcAccount mapping String to Enum
                        .balance(hsbcAccount.getBalance().getAmount())
                        .currency(hsbcAccount.getCurrency())
                        .description(hsbcAccount.getDescription())
                        .bank(hsbc)
                        .build();
                accounts.add(account);

                accountServicesRegistry.registerTransactionService(account, this);
            }
        }
        return accounts;
    }

    public List<Transaction> getTransactions(Account account) {
        final List<Transaction> transactions = new ArrayList<>();

        if (HsbcUserAccessToken.current() != null) {
            hsbcAuth.validateTokenExpiry(HsbcUserAccessToken.current());
            final HsbcTransactionObject hsbcTransactionObject =
                    getTransactions(account.getAccountId(), HsbcUserAccessToken.current());

            // @todo use mapStruct mapper
            for (HsbcTransaction hsbcTransaction : hsbcTransactionObject.getData().getTransactionList()) {
                Transaction transaction = Transaction.builder()
                        .date(new Date()) // @todo map dates from hsbc txn
                        .description(hsbcTransaction.getTransactionId())
                        .type("-")
                        .category("-")
                        .amount(new BigDecimal(hsbcTransaction.getHsbcAmount().getAmount()))
                        .build();
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    // Private methods - handling of the OAuth flows is not exposed

    // curl -v -X GET --cert qwac.der --cert-type DER --key server.key
    // -H "x-fapi-financial-id: test"
    // -H "Authorization: Bearer 4501825f-096a-4959-bde7-dff93a5fe6ba"
    // -H "Cache-Control: no-cache"
    // "https://sandbox.hsbc.com/psd2/obie/v3.1/accounts"
    private List<HsbcAccount> getUserAccounts(HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<HsbcAccountList> response =
                restTemplate.exchange(ACCOUNT_INFO_URL, HttpMethod.GET, request, HsbcAccountList.class);

        // sets account balances with a rest call per account
        for (HsbcAccount account : response.getBody().getData().getAccounts()) {
            account.setBalance(getAccountBalance(account, accessToken));
        }

        log.debug("User Accounts ---------" + response.getBody());

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
    private void getAccountDetails(HsbcAccount account, HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(ACCOUNT_INFO_URL + "/" + account.getAccountId(),
                        HttpMethod.GET, request, String.class);

        log.debug("User Accounts for AccountID ---------" + response.getBody());
    }

    // "GET /accounts/{AccountId}/balances"
    private Balance getAccountBalance(HsbcAccount account, HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<HsbcBalanceObject> response =
                restTemplate.exchange(ACCOUNT_INFO_URL + "/" + account.getAccountId() + "/balances",
                        HttpMethod.GET, request, HsbcBalanceObject.class);

        log.debug("Balance for AccountID ---------" + response.getBody());

        Balance balance = new Balance(
                response.getBody().getData().getBalanceList().get(0).getAmount().getAmount(),
                response.getBody().getData().getBalanceList().get(0).getCreditDebit().equals("Credit"),
                response.getBody().getData().getBalanceList().get(0).getAmount().getCurrency());

        return balance;
    }

    // "GET /accounts/{AccountId}/transactions"
    private HsbcTransactionObject getTransactions(String accountId, HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<HsbcTransactionObject> response =
                restTemplate.exchange(ACCOUNT_INFO_URL + "/" + accountId + "/transactions",
                        HttpMethod.GET, request, HsbcTransactionObject.class);

        log.debug("Transactions for AccountID ---------" + response.getBody());
        return response.getBody();
    }
}
