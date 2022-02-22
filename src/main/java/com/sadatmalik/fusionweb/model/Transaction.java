package com.sadatmalik.fusionweb.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Transaction {
    private Date date;
    private String description;
    private String type; // e.g. DD, SO, TFR
    private String category; // e.g. GROCERY, SALARY
    private BigDecimal amount;

    public String printAmount() {
        return String.format("£%.2f", amount);
    }
}
