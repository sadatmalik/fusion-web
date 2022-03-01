package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> getAccountsFor(User user) {
        return accountRepository.findByUser(user);
    }

    public Account getAccountById(String accountId) {
        return accountRepository.findByAccountId(accountId);
    }

    public Account saveUserAccount(User user, Account account) {
        account.setUser(user);
        return accountRepository.save(account);
    }

    public Account getAccountByAccountId(String accountId) {
        return accountRepository.findByAccountId(accountId);
    }
}
