package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcAuthenticationService;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcClientAccessToken;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcConsent;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcUserAccessToken;
import com.sadatmalik.fusionweb.services.AccountService;
import com.sadatmalik.fusionweb.services.hsbc.HsbcApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HsbcController {

    private final HsbcAuthenticationService hsbcAuth;
    private final HsbcApiService hsbc;
    private final AccountService accountService;

    private HsbcClientAccessToken clientAccessToken;

    @GetMapping("/hsbc")
    public String hsbcAuthorizationUrl() {
        // @todo client access token belongs to creative fusion - store in DB?
        if (clientAccessToken == null) {
            clientAccessToken = hsbcAuth.getAccessToken();
        }

        HsbcConsent consent = hsbcAuth.getConsentID(clientAccessToken);
        String authorizationURL = hsbcAuth.getAuthorizationURL(consent);
        log.debug("Redirecting to HSBC auth URL: " + authorizationURL);

        return "redirect:" + authorizationURL;
    }

    @GetMapping({"", "/"})
    public String hsbcCallback(@RequestParam(name = "code", required = false) String authCode,
                               Authentication authentication) {
        if (authCode != null) {
            log.debug("Received authCode: " + authCode);

            // try load hsbc accounts adding to logged-in user's accounts
            try {
                HsbcUserAccessToken.setCurrentToken(hsbcAuth.getAccessToken(authCode));
                loadUserAccounts(authentication);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "redirect:/dashboard";
        }

        log.info("Redirecting to Quickstats controller");
        return "redirect:/quickstats";
    }

    private void loadUserAccounts(Authentication authentication) {
        List<Account> accounts = hsbc.getUserAccounts();

        User user = Utils.getUser(authentication);
        for (Account account : accounts) {
            accountService.saveUserAccount(user, account);
        }
    }
}
