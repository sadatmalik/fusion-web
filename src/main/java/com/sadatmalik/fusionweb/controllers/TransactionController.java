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

/**
 * Spring MVC controller that handles all transaction processing related endpoints,
 * interacting with the transaction related view templates, via the standard Spring
 * MVC model.
 *
 * @author sadatmalik
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final AccountService accountService;

    private final AccountServicesRegistry accountServicesRegistry;

    /**
     * Listens for requests to the /transactions/{accountId endpoint}. It looks up the
     * corresponding account for the received accountId via the {@code AccountService}
     * instance.
     *
     * Formats the account balance and attaches this to the returned MVC model. Together
     * with the account object itself. Uses the {@code AccountServiceRegistry} to find the
     * {@code TransactionService} connected to the {@code Account}. Then requests all
     * transactions for the account from the {@code TransactionService}.
     *
     * Attached the returned transactions list to the MVC model for display.
     *
     * @param model injected by the Spring context.
     * @param accountId received as a path variable with the HTTP GET request.
     * @return returns the account-transactions view
     */
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

    /**
     * Listens for requests to the /transactions endpoint. It looks up all accounts
     * via the {@code AccountService} instance for the user corresponding to the current
     * authentication.
     *
     * Calculates and formats the total account balance for all user accounts and attaches
     * this to the returned MVC model.
     *
     * Uses the {@code AccountServiceRegistry} to find the {@code TransactionService}
     * connected to each {@code Account} belonging to the user. Requesting all transactions
     * for each account from the {@code TransactionService} and returning the full list of
     * transactions across all accounts in the MVC model for display.
     *
     * @param model injected by the Spring context.
     * @param authentication provided by the Spring context.
     * @return returns the transactions view.
     */
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
