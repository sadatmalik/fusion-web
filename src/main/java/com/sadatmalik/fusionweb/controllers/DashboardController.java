package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Spring MVC controller that handles endpoints related to the application dashboard,
 * which provides the user with an overview of all connected user accounts.
 *
 * @author sadatmalik
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final AccountService accountService;

    /**
     * Loads the accounts for the currently authenticated user via the central account
     * service, injected from the Spring application context.
     *
     * Populates the MVC Model with the retrieved accounts, and populates a formatted
     * total balance for all retrieved accounts.
     *
     * @param authentication provided by the Spring context
     * @param model provided by the Spring context
     * @return returns the "dashboard" view
     */
    @GetMapping("/dashboard")
    public String getDashboard(Authentication authentication, Model model) {

        // get logged in users accounts
        log.debug("Authentication: " + authentication);

        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        List<Account> accounts = accountService.getAccountsFor(user);

        String totalBalance = Utils.getTotalDisplayBalance(accounts);
        model.addAttribute("accountList", accounts);
        model.addAttribute("totalBalance", totalBalance);

        model.addAttribute("date",
                LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM, dd yyyy")));

        return "dashboard";
    }

}
