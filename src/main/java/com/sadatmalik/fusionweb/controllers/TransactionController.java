package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.Transaction;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.AccountService;
import com.sadatmalik.fusionweb.services.AccountServicesRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final AccountService accountService;

    private final AccountServicesRegistry accountServicesRegistry;

    @GetMapping("/transactions/{accountId}")
    public String transactionsByAccount(Model model, @PathVariable String accountId) {

        Account account = accountService.getAccountById(accountId);

        // @todo exception case - account not exists
        log.debug("Found - " + account);

        String totalBalance = String.format("£%,.2f", account.getBalance()); // @todo refactor to display method
        model.addAttribute("account", account);
        model.addAttribute("totalBalance", totalBalance);

        model.addAttribute("transactions",
                accountServicesRegistry.getTransactionServiceFor(account).getTransactions(account));

        return "account-transactions";
    }

    @GetMapping("/transactions")
    public String allTransactions(Model model, Authentication authentication) {
        // Get logged-in User's accounts
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        List<Account> accounts = accountService.getAccountsFor(user);

        model.addAttribute("totalBalance", Utils.getTotalDisplayBalance(accounts));

        List<Transaction> transactionList = new ArrayList<>();
        for (Account account : accounts) {
            transactionList.addAll(
                    accountServicesRegistry.getTransactionServiceFor(account)
                            .getTransactions(account)
            );
        }
        model.addAttribute("transactions", transactionList);

        return "transactions";
    }
}
