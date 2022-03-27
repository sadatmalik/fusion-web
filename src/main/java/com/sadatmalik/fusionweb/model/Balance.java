package com.sadatmalik.fusionweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA entity class that models a bank account balance.
 *
 * @author sadatmalik
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Balance {
    private double amount;
    private boolean isCredit;
    private String currency;

    public String getDisplayAmount() {
        return String.format("£%.2f", amount);
    }
}
