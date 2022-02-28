package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// a dummy service to mock some transactions
@Service
public class DummyBarclaysSavingsTransactionService implements TransactionService {

    public List<Transaction> getTransactions(Account account) {
        List txns = new ArrayList();

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("BARCLAYS CURRENT ACCOUNT")
                .category("OUTGOING")
                .amount(new BigDecimal(-220))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("INT")
                .description("INTEREST")
                .category("INTEREST")
                .amount(new BigDecimal(0.32))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("BARCLAYS CURRENT ACCOUNT")
                .category("OUTGOING")
                .amount(new BigDecimal(-160))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("INT")
                .description("INTEREST")
                .category("INTEREST")
                .amount(new BigDecimal(0.37))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("BARCLAYS CURRENT ACCOUNT")
                .category("OUTGOING")
                .amount(new BigDecimal(-558.45))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("S MALIK CURRENT")
                .category("SAVING")
                .amount(new BigDecimal(500))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("INT")
                .description("INTEREST")
                .category("INTEREST")
                .amount(new BigDecimal(0.31))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("S MALIK CURRENT")
                .category("SAVING")
                .amount(new BigDecimal(2000))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("BARCLAYS CURRENT ACCOUNT")
                .category("OUTGOING")
                .amount(new BigDecimal(-100))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("INT")
                .description("INTEREST")
                .category("INTEREST")
                .amount(new BigDecimal(0.31))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("BARCLAYS CURRENT ACCOUNT")
                .category("OUTGOING")
                .amount(new BigDecimal(-100))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("S MALIK CURRENT")
                .category("SAVING")
                .amount(new BigDecimal(1240))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("INT")
                .description("INTEREST")
                .category("INTEREST")
                .amount(new BigDecimal(0.27))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("S MALIK CURRENT")
                .category("SAVING")
                .amount(new BigDecimal(2750))
                .build());

        return txns;
    }
}
