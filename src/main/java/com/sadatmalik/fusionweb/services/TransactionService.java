package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.Transaction;

import java.util.List;

/**
 * Common methods that all transaction service classes must implement.
 *
 * @author sadatmalik
 */
public interface TransactionService {
    List<Transaction> getTransactions(Account account);
}
