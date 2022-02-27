package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountServicesRegistry {

    private static AccountServicesRegistry instance;

    // @todo refactor to Map<Account, TransactionService>
    private final Map<String, TransactionService> transactionServiceByAccount;

    private AccountServicesRegistry() {
        transactionServiceByAccount = new HashMap<>();
    }

    public static AccountServicesRegistry getInstance() {
        if (instance == null) {
            instance = new AccountServicesRegistry();
        }
        return instance;
    }

    public void registerTransactionService(Account account, TransactionService transactionService) {
        transactionServiceByAccount.put(account.getAccountId(), transactionService);
    }

    public TransactionService getTransactionServiceFor(Account account) {
        return transactionServiceByAccount.get(account.getAccountId());
    }
}
