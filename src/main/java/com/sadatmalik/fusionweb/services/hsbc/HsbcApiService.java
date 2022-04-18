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

/**
 * This service handles all the communication with the Hsbc OB API. Uses the Hsbc
 * authentication service to ensure rest calls are authenticated. Uses an injected
 * RestTemplate, which has been secured in the RestTemplateCustomizer against the
 * far end Api requirements, to furnish Http request/response exchanges.
 *
 * The service implements the HsbcOpenBankingService and TransactionService
 * interfaces.
 *
 * @see org.springframework.boot.web.client.RestTemplateCustomizer
 *
 * @author sadatmalik
 */
@Slf4j
@Service
public class HsbcApiService implements HsbcOpenBankingService, TransactionService {
    private final HsbcAuthenticationService hsbcAuth;
    private final RestTemplate restTemplate;
    private final BankRepository bankRepository;
    private final AccountServicesRegistry accountServicesRegistry;

    private final Bank hsbc;

    /**
     * Non-default constructor.
     *
     * @param hsbcAuth injected Hsbc Authentication service object.
     * @param restTemplate injected customized RestTemplate.
     * @param accountServicesRegistry injected account registry object.
     * @param bankRepository injected bank repository.
     */
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

    /**
     * Checks that the user authentication token is non-expired (implicitly refreshing
     * if it has expired), then calls the getUserAccounts(..) method which makes a Rest
     * call to the far end to retrieve the permissioned accounts for the current
     * authentication.
     *
     * Maps the received account information to the Fusion Account model.
     *
     * Registers itself (this) as the transaction service for each returned account.
     * This means that future queries for transaction related information for the given
     * Fusion account will be handled by this service.
     *
     * @return List of Account objects.
     */
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

    /**
     * Retrieves the transactions for the given Fusion account.
     *
     * Implicitly checks and refreshes the user access token before making the Rest
     * call. Converts the received transactions to the Fusion transaction model.
     *
     * @param account the user account.
     * @return list of transactions for the given user account.
     */
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

    // Private methods

    /**
     * Codifies the following Curl:
     *
     *     curl -v -X GET --cert qwac.der --cert-type DER --key server.key
     *     -H "x-fapi-financial-id: test"
     *     -H "Authorization: Bearer 4501825f-096a-4959-bde7-dff93a5fe6ba"
     *     -H "Cache-Control: no-cache"
     *     "https://sandbox.hsbc.com/psd2/obie/v3.1/accounts"
     *
     * @param accessToken the user access token.
     * @return list of account transactions for the given user access token.
     */
    private List<HsbcAccount> getUserAccounts(HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<HsbcAccountList> response =
                restTemplate.exchange(ACCOUNT_INFO_URL, HttpMethod.GET, request, HsbcAccountList.class);

        // sets account balances with a rest call per account
        if (response.hasBody()) {
            for (HsbcAccount account : response.getBody().getData().getAccounts()) {
                account.setBalance(getAccountBalance(account, accessToken));
            }

            log.debug("User Accounts ---------" + response.getBody());

            return response.getBody().getData().getAccounts();
        } else {
            // todo consider an exception here?
            return null;
        }
    }

    private HttpHeaders getGetHeaders(HsbcUserAccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-fapi-financial-id", "test");
        headers.setBearerAuth(accessToken.getAccessToken());
        headers.setCacheControl(CacheControl.noCache());

        return headers;
    }

    /**
     * Calls the following endpoint: "GET /accounts/{AccountId}"
     *
     * @param account hsbc account containing the accountId to query.
     * @param accessToken the user access token for this query.
     */
    private void getAccountDetails(HsbcAccount account, HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(ACCOUNT_INFO_URL + "/" + account.getAccountId(),
                        HttpMethod.GET, request, String.class);

        log.debug("User Accounts for AccountID ---------" + response.getBody());
    }

    /**
     * Calls the following endpoint: "GET /accounts/{AccountId}/balances"
     *
     * @param account hsbc account containing the accountId to query.
     * @param accessToken the user access token for this query.
     * @return the account balance for the given account.
     */
    private Balance getAccountBalance(HsbcAccount account, HsbcUserAccessToken accessToken) {
        HttpHeaders headers = getGetHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<HsbcBalanceObject> response =
                restTemplate.exchange(ACCOUNT_INFO_URL + "/" + account.getAccountId() + "/balances",
                        HttpMethod.GET, request, HsbcBalanceObject.class);

        log.debug("Balance for AccountID ---------" + response.getBody());

        if (response.hasBody()) {
            Balance balance = new Balance(
                    response.getBody().getData().getBalanceList().get(0).getAmount().getAmount(),
                    response.getBody().getData().getBalanceList().get(0).getCreditDebit().equals("Credit"),
                    response.getBody().getData().getBalanceList().get(0).getAmount().getCurrency());

            return balance;
        } else {
            // todo consider an exception here?
            return null;
        }
    }

    /**
     * Calls the following endpoint: "GET /accounts/{AccountId}/transactions"
     *
     * @param accountId hsbc accountId to query.
     * @param accessToken the user access token for this query.
     * @return an object representing the returned Json transactions.
     */
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
