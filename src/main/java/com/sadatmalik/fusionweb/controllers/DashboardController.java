package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.AccountService;
import com.sadatmalik.fusionweb.services.TransactionService;
import com.sadatmalik.fusionweb.services.hsbc.HsbcApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final HsbcApiService hsbc;
    private final TransactionService transactionService;
    private final AccountService accountService;


    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {

        // get logged in users accounts
        log.debug("Authentication: " + authentication);

        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        List<Account> accounts = accountService.getAccountsFor(user);

        String totalBalance = getTotalDisplayBalance(accounts);
        model.addAttribute("accountList", accounts);
        model.addAttribute("totalBalance", totalBalance);

        model.addAttribute("date",
                LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM, dd yyyy")));

        return "dashboard";
    }

    private String getTotalDisplayBalance(List<Account> accounts) {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return String.format("£%.2f", total);
    }

    @GetMapping("/transactions/{accountId}")
    public String viewTransactions(Model model, @PathVariable String accountId) {

        Account account = accountService.getAccountById(accountId);

        // @todo exception case - account not exists
        log.debug("Found - " + account);

        String totalBalance = String.format("£%.2f", account.getBalance()); // @todo refactor to display method
        model.addAttribute("account", account);
        model.addAttribute("totalBalance", totalBalance);

        // @todo this will fail as passing accountId for DB accounts at moment
        model.addAttribute("transactions", hsbc.getTransactions(accountId));
        //model.addAttribute("transactions", transactionService.getTransactions());

        return "account-transactions";
    }
}
