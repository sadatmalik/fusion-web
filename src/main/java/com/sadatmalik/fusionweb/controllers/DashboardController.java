package com.sadatmalik.fusionweb.controllers;

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
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final HsbcApiService hsbc;
    private final TransactionService transactionService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // get HSBC account info
        List<Account> accounts = hsbc.getUserAccounts();

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
        hsbc.getTransactions(accountId);

        // @todo refactor - is this call to Hsbc Apis necessary - at this
        // stage accounts are available from currently logged in user?
        List<Account> accounts = hsbc.getUserAccounts();
        Optional<Account> account = accounts.stream()
                .filter(a -> a.getAccountId().equals(accountId))
                .findFirst();

        // @todo exception case - account not exists
        log.debug("Found - " + account.get());

        String totalBalance = getTotalDisplayBalance(List.of(account.get()));
        model.addAttribute("account", account.get());
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("transactions", transactionService.getTransactions());

        return "account-transactions";
    }
}
