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

/**
 * This is the Spring MVC controller that handles endpoints that are intended for
 * interaction with the HSBC Open Banking API.
 *
 * When the user chooses to connect an HSBC account, the /hsbc endpoint will be hit,
 * this will initiate the OAuth flow process that retrieves from HSBC a secure and
 * authenticated user account authorization URL.
 *
 * The user is then forwarded to HSBC where they can select the accounts that they
 * wish to authorize for access from Fusion.
 *
 * The HSBC Open Banking API will then return the user to the Fusion application
 * redirect URL submitted in the OAuth authentication sequence. This arrives back
 * at the hsbcCallback method defined in the controller below.
 *
 * These two primary methods may be factored out to a clean interface in a future
 * release. Essentially, what is needed is a getAuthorizationUrl() method; and an
 * openBankingCallback() method.
 *
 * @see #hsbcAuthorizationUrl()
 * @see #hsbcCallback(String, Authentication)
 * @author sadatmalik
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class HsbcController {

    private final HsbcAuthenticationService hsbcAuth;
    private final HsbcApiService hsbc;
    private final AccountService accountService;

    private HsbcClientAccessToken clientAccessToken;

    /**
     * When the user hits this endpoint, the intention is to connect out to the Open Banking
     * API implementation at HSBC. It initiates an OAuth sequence that will end, if
     * successful, with the return of a secure, authenticated URL, which the user will be
     * redirected to, so that they can authorize access for Fusion to whichever accounts they
     * wish to grant permission for.
     *
     * Note that the clientAccessToken is Fusion's own access token to the OB API. It does
     * not therefore need to be requested on each occasion. It may be prudent to handle this once
     * on application startup; and to then re-request as needed based on token expiry. In a live
     * environment the application server will be continuously running so this renewals can be
     * handled effectively as needed.
     *
     * A user consent can then be retrieved, and this is followed by a request for the
     * authorisation URL, to which the user in then redirected.
     *
     * @return redirect to HSBC authorization portal
     */
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

    /**
     * This is the endpoint that is hit upon returning from the Open Banking API
     * flow. The callback will have within it an authorisation code sent as a URL request
     * parameter. This is to be retrieved and maintained, as it will be required on
     * all subsequent API requests made to the Open Banking API on behalf of the
     * current user.
     *
     * The authorisation code is saved as current in the HsbcUserAccessToken, where
     * its expiry time will be tracked and refreshed as required.
     *
     * A call to load the authorised account data for this user is then initiated, which
     * in turn will request the relevant information from the Open Banking API.
     *
     * Returned account data for the current user will be saved and is now available in
     * Fusion.
     *
     * @param authCode authorisation code returned by Hsbc in the app callback
     * @param authentication provided by the Spring context
     * @return redirects to the accounts overview dashboard
     */
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

        // @todo redirect to quickstats or stay on dashboard ?
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
