package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Simple utility class for methods commonly used across the controller classes.
 *
 * @author sadatmalik
 */
@UtilityClass
public class Utils {

    /**
     * Calculates the total balance for all accounts in the accounts list.
     * Formats the total balance returning a String value. Commonly used by
     * controllers when attaching balance value to the MVC model.
     *
     * @param accounts list of accounts.
     * @return returns total account balance as a formatted String.
     */
     static String getTotalDisplayBalance(List<Account> accounts) {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return String.format("£%,.2f", total);
    }

    /**
     * A commonly reused lookup method that returns the User corresponding to the
     * Spring security authentication injected in to the upstream controller via the
     * Spring security context.
     *
     * @param authentication provided by the current security context to upstream
     *                       controllers
     * @return
     */
    static User getUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }
}