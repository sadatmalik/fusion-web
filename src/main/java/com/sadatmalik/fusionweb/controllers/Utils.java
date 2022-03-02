package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;

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

    public static User getUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }
}