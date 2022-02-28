package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Utils {

    public static String getTotalDisplayBalance(List<Account> accounts) {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return String.format("£%,.2f", total);
    }
}