package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.oauth.hsbc.HsbcAuthenticationService;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcUserAccessToken;
import com.sadatmalik.fusionweb.services.TransactionService;
import com.sadatmalik.fusionweb.services.hsbc.HsbcApiService;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final HsbcAuthenticationService hsbcAuth;
    private final HsbcApiService hsbc;
    private final TransactionService transactionService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Account> accounts = new ArrayList<>();

        // get HSBC account info
        if (HsbcUserAccessToken.current() != null) {
            hsbcAuth.validateTokenExpiry(HsbcUserAccessToken.current());
            accounts = hsbc.getUserAccounts(HsbcUserAccessToken.current());
        }

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
            total += account.getBalance().getAmount();
        }
        return String.format("£%.2f", total);
    }

    @GetMapping("/transactions/{accountId}")
    public String viewTransactions(Model model, @PathVariable String accountId) {
        hsbcAuth.validateTokenExpiry(HsbcUserAccessToken.current());
        hsbc.getTransactions(accountId, HsbcUserAccessToken.current());

        // @todo refactor - duplicated code in dashboard method
        List<Account> accounts = hsbc.getUserAccounts(HsbcUserAccessToken.current());
        Optional<Account> account = accounts.stream()
                .filter(a -> a.getAccountId().equals(accountId))
                .findFirst();

        log.debug("Found - " + account.get());

        String totalBalance = getTotalDisplayBalance(List.of(account.get()));
        model.addAttribute("account", account.get());
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("transactions", transactionService.getTransactions());

        return "account-transactions";
    }
}
