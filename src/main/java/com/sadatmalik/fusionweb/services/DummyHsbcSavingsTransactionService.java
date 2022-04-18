package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A dummy service that mocks some transaction data for test and demo purposes.
 *
 * @author sadatmalik
 */
@Service
public class DummyHsbcSavingsTransactionService implements TransactionService {

    /**
     * Overrides and implements the getTransactions method. Returns a list of
     * transactions for the given account.
     *
     * @param account the account.
     * @return a list of transactions for the given account.
     */
    public List<Transaction> getTransactions(Account account) {
        List txns = new ArrayList();

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("HSBC CURRENT ACCOUNT")
                .category("OUTGOING")
                .amount(new BigDecimal(-300))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("INT")
                .description("INTEREST")
                .category("INTEREST")
                .amount(new BigDecimal(0.16))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("HMRC")
                .category("TAX")
                .amount(new BigDecimal(-3256.37))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("HSBC CURRENT ACCOUNT")
                .category("OUTGOING")
                .amount(new BigDecimal(-440))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("HSBC CURRENT ACCOUNT")
                .category("OUTGOING")
                .amount(new BigDecimal(-500))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("INT")
                .description("INTEREST")
                .category("INTEREST")
                .amount(new BigDecimal(0.18))
                .build());

//        txns.add(Transaction.builder()
//                .date(new Date())
//                .type("TFR")
//                .description("S MALIK CURRENT")
//                .category("SAVING")
//                .amount(new BigDecimal(500))
//                .build());
//
//        txns.add(Transaction.builder()
//                .date(new Date())
//                .type("INT")
//                .description("INTEREST")
//                .category("INTEREST")
//                .amount(new BigDecimal(0.31))
//                .build());
//
//        txns.add(Transaction.builder()
//                .date(new Date())
//                .type("TFR")
//                .description("S MALIK CURRENT")
//                .category("SAVING")
//                .amount(new BigDecimal(2000))
//                .build());
//
//        txns.add(Transaction.builder()
//                .date(new Date())
//                .type("TFR")
//                .description("BARCLAYS CURRENT ACCOUNT")
//                .category("OUTGOING")
//                .amount(new BigDecimal(-100))
//                .build());
//
//        txns.add(Transaction.builder()
//                .date(new Date())
//                .type("INT")
//                .description("INTEREST")
//                .category("INTEREST")
//                .amount(new BigDecimal(0.31))
//                .build());
//
//        txns.add(Transaction.builder()
//                .date(new Date())
//                .type("TFR")
//                .description("BARCLAYS CURRENT ACCOUNT")
//                .category("OUTGOING")
//                .amount(new BigDecimal(-100))
//                .build());
//
//        txns.add(Transaction.builder()
//                .date(new Date())
//                .type("TFR")
//                .description("S MALIK CURRENT")
//                .category("SAVING")
//                .amount(new BigDecimal(1240))
//                .build());
//
//        txns.add(Transaction.builder()
//                .date(new Date())
//                .type("INT")
//                .description("INTEREST")
//                .category("INTEREST")
//                .amount(new BigDecimal(0.27))
//                .build());
//
//        txns.add(Transaction.builder()
//                .date(new Date())
//                .type("TFR")
//                .description("S MALIK CURRENT")
//                .category("SAVING")
//                .amount(new BigDecimal(2750))
//                .build());

        return txns;
    }
}
