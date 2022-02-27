package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactions(Account account);
}
