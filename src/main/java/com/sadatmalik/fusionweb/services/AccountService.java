package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service encapsulates  methods that clients of the service can call for
 * Crud and other account related common behaviours and requests.
 *
 * Uses an instance of AccountRepository for DB interactions.
 *
 * @author sadatmalik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Get a list of all accounts for the given user.
     *
     * @param user the user.
     * @return a list of accounts for the given user.
     */
    public List<Account> getAccountsFor(User user) {
        return accountRepository.findByUser(user);
    }

    /**
     * Get and return the single account with the given accound id.
     *
     * @param accountId the account id to query.
     * @return the account with the given account id.
     */
    public Account getAccountById(String accountId) {
        return accountRepository.findByAccountId(accountId);
    }

    /**
     * Save the given account details for the given user.
     *
     * @param user the user owning the account.
     * @param account the account.
     * @return the saved account.
     */
    public Account saveUserAccount(User user, Account account) {
        account.setUser(user);
        return accountRepository.save(account);
    }
}
