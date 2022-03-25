package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountServicesRegistry {

    // @todo refactor to Map<Account, TransactionService>
    private final Map<String, TransactionService> transactionServiceByAccount;

    private AccountServicesRegistry() {
        transactionServiceByAccount = new HashMap<>();
    }

    public void registerTransactionService(Account account, TransactionService transactionService) {
        transactionServiceByAccount.put(account.getAccountId(), transactionService);
    }

    public TransactionService getTransactionServiceFor(Account account) {
        return transactionServiceByAccount.get(account.getAccountId());
    }
}
