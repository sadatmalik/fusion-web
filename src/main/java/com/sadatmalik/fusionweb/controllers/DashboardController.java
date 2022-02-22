package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.oauth.hsbc.HsbcAuthenticationService;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcClientAccessToken;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcConsent;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcUserAccessToken;
import com.sadatmalik.fusionweb.services.hsbc.HsbcApiService;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.Account;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    // todo keep this with user details
    private HsbcUserAccessToken userAccessToken;

    @Autowired
    HsbcAuthenticationService hsbcAuth;

    @Autowired
    HsbcApiService hsbc;

    @GetMapping({"", "/"})
    public String home(@RequestParam(name = "code", required = false) String authCode) {
        logger.info("Returning index page");
        if (authCode != null) {
            System.out.println("Received authCode: " + authCode);
            userAccessToken = hsbcAuth.getAccessToken(authCode);
            return "redirect:/dashboard";
        }
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // @todo integrate this into UserDetails
        if (userAccessToken == null) {
            HsbcClientAccessToken accessToken = hsbcAuth.getAccessToken();
            HsbcConsent consent = hsbcAuth.getConsentID(accessToken);

            String authorizationURL = hsbcAuth.getAuthorizationURL(consent);
            System.out.println(authorizationURL);

            return "redirect:" + authorizationURL;
        }
        // @todo handle the refresh in UserDetails on a timer?
        else if (userAccessToken.isExpiring()) {
            userAccessToken = hsbcAuth.refreshAccessToken(userAccessToken);
        }

        List<Account> accounts = hsbc.getUserAccounts(userAccessToken);
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
        // @todo encapsulate this elsewhere - with user details?
        if (userAccessToken.isExpiring()) {
            userAccessToken = hsbcAuth.refreshAccessToken(userAccessToken);
        }

        hsbc.getTransactions(accountId, userAccessToken);

        // @todo refactor - duplicated code in dashboard method
        List<Account> accounts = hsbc.getUserAccounts(userAccessToken);
        String totalBalance = getTotalDisplayBalance(accounts);
        model.addAttribute("accountList", accounts);
        model.addAttribute("totalBalance", totalBalance);

        return "account-transactions";
    }
}
