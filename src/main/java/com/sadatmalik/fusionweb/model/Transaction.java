package com.sadatmalik.fusionweb.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JPA entity class that models a single transaction for a particular user
 * Account.
 *
 * @author sadatmalik
 */
@Data
@Builder
public class Transaction {

    private static final DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy");

    private Date date;
    private String description;
    private String type; // e.g. DD, SO, TFR
    private String category; // e.g. GROCERY, SALARY
    private BigDecimal amount;

    public String displayAmount() {
        return String.format("£%,.2f", amount);
    }

    public String displayDate() {
        return dateFormat.format(date);
    }
}
