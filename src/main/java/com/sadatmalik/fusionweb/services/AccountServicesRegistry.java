package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple registry class that maps services by account.
 *
 * For example maintains a map of transaction services for a given account. Each
 * transaction service will be a concrete implementation of the TransactionService
 * interface.
 *
 * Users of the registry can retrieve the transaction service instance for the given
 * account. Users of the registry may also register a transaction service for a
 * given account.
 *
 * @author sadatmalik
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountServicesRegistry {

    // @todo refactor to Map<Account, TransactionService>
    private final Map<String, TransactionService> transactionServiceByAccount;

    private AccountServicesRegistry() {
        transactionServiceByAccount = new HashMap<>();
    }

    /**
     * Register a transaction service for the given account.
     *
     * @param account the account.
     * @param transactionService the transaction service.
     */
    public void registerTransactionService(Account account, TransactionService transactionService) {
        transactionServiceByAccount.put(account.getAccountId(), transactionService);
    }

    /**
     * Retrieve the registered transaction service for the given account.
     *
     * @param account the account.
     * @return the transaction service registered for the given account.
     */
    public TransactionService getTransactionServiceFor(Account account) {
        return transactionServiceByAccount.get(account.getAccountId());
    }
}
