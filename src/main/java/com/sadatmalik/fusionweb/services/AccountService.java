package com.sadatmalik.fusionweb.services;

import brave.ScopedSpan;
import brave.Tracer;
import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service encapsulates  methods that clients of the service can call for Crud and
 * other account related common behaviours and requests.
 *
 * Uses an instance of AccountRepository for DB interactions.
 *
 * Uses a Tracer to allow for scoped spans of DB interactions for log tracing purposes.
 *
 * @author sadatmalik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final Tracer tracer;
    private static final String PEER_SERVICE_NAME = "fusion-mysql";

    /**
     * Get a list of all accounts for the given user. Traces the DB call time.
     *
     * @param user the user.
     * @return a list of accounts for the given user.
     */
    public List<Account> getAccountsFor(User user) {
        ScopedSpan newSpan = tracer.startScopedSpan("get-accounts-for-user-db-call");
        List<Account> accounts;
        try {
            accounts = accountRepository.findByUser(user);
            if (accounts.isEmpty()) {
                log.warn("Unable to find any accounts for user: " + user);
            } else {
                log.debug("Got accounts for user: " + user + ": " + accounts);
            }
        } finally {
            newSpan.tag("peer.service", PEER_SERVICE_NAME);
            newSpan.annotate("get.accounts.by.user");
            newSpan.finish();
        }
        return accounts;
    }

    /**
     * Get and return the single account with the given account id. Traces the DB
     * call time.
     *
     * @param accountId the account id to query.
     * @return the account with the given account id.
     */
    public Account getAccountById(String accountId) {
        ScopedSpan newSpan = tracer.startScopedSpan("get-account-by-id-db-call");
        Account account;
        try {
            account = accountRepository.findByAccountId(accountId);
            if (account == null) {
                log.warn("Unable to find any account for account id " + accountId);
            } else {
                log.debug("Got account with account id " + accountId + ": " + account);
            }
        } finally {
            newSpan.tag("peer.service", PEER_SERVICE_NAME);
            newSpan.annotate("get.account.by.id");
            newSpan.finish();
        }
        return account;
    }

    /**
     * Save the given account details for the given user.
     *
     * @param user the user owning the account.
     * @param account the account.
     * @return the saved account.
     */
    public Account saveUserAccount(User user, Account account) {
        ScopedSpan newSpan = tracer.startScopedSpan("get-account-by-id-db-call");
        Account saved;
        try {
            account.setUser(user);
            saved = accountRepository.save(account);
            if (saved == null) {
                log.warn("Unable to save account to DB: " + account);
            } else {
                log.debug("Saved account to DB: " + saved);
            }
        } finally {
            newSpan.tag("peer.service", PEER_SERVICE_NAME);
            newSpan.annotate("save.user.account");
            newSpan.finish();
        }
        return saved;
    }
}
