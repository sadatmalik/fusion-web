package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// a dummy service to mock some transactions
@Service
public class TransactionService {

    public List<Transaction> getTransactions() {
        List txns = new ArrayList();

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("WESTGATE OXFORD PARKING")
                .category("TRANSPORT")
                .amount(new BigDecimal(5.00))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("COLOMBIA COFFEE")
                .category("FOOD & DRINK")
                .amount(new BigDecimal(3.07))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("DD")
                .description("THAMES WATER")
                .category("UTILITIES")
                .amount(new BigDecimal(40))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("DELIVEROO")
                .category("FOOD & DRINK")
                .amount(new BigDecimal(33.44))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("PAPPO LIMITED")
                .category("FOOD & DRINK")
                .amount(new BigDecimal(3.50))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("CO OPERATIVE FOOD")
                .category("GROCERIES")
                .amount(new BigDecimal(10.59))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("PRET A MANGER")
                .category("FOOD & DRINK")
                .amount(new BigDecimal(6.45))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("OXFORD PHILHARMONIC")
                .category("ENTERTAINMENT")
                .amount(new BigDecimal(26))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("WAITROSE")
                .category("GROCERIES")
                .amount(new BigDecimal(16.06))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("TV LICENCE")
                .category("BILLS")
                .amount(new BigDecimal(13.25))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("SAINSBURYS PETROL")
                .category("TRAVEL")
                .amount(new BigDecimal(7.91))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("SCOTTISH POWER")
                .category("UTILITIES")
                .amount(new BigDecimal(178))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("VIS")
                .description("TALK TALK BROADBAND")
                .category("UTILITIES")
                .amount(new BigDecimal(31.95))
                .build());

        txns.add(Transaction.builder()
                .date(new Date())
                .type("TFR")
                .description("SAVILLS")
                .category("PROPERTY")
                .amount(new BigDecimal(1830))
                .build());


        return txns;
    }
}
