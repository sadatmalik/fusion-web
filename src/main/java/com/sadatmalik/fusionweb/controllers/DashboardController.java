package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.oauth.HsbcAuthenticationService;
import com.sadatmalik.fusionweb.oauth.HsbcClientAccessToken;
import com.sadatmalik.fusionweb.oauth.HsbcConsent;
import com.sadatmalik.fusionweb.oauth.HsbcUserAccessToken;
import com.sadatmalik.fusionweb.services.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        hsbc.getAccountBalance(accounts.get(0), userAccessToken);
        model.addAttribute("accountList", accounts);

        return "dashboard";
    }
}
