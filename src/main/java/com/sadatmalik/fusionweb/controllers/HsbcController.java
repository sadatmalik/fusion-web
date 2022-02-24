package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.oauth.hsbc.HsbcAuthenticationService;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcClientAccessToken;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcConsent;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcUserAccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HsbcController {

    private final HsbcAuthenticationService hsbcAuth;

    private HsbcClientAccessToken clientAccessToken;

    @GetMapping("/hsbc")
    public String connectHsbc(Model model) {
        // @todo integrate this into UserDetails
        if (clientAccessToken == null) {
            clientAccessToken = hsbcAuth.getAccessToken();
        }

        HsbcConsent consent = hsbcAuth.getConsentID(clientAccessToken);
        String authorizationURL = hsbcAuth.getAuthorizationURL(consent);
        log.debug("Redirecting to HSBC auth URL: " + authorizationURL);

        return "redirect:" + authorizationURL;
    }

    @GetMapping({"", "/"})
    public String hsbcCallback(@RequestParam(name = "code", required = false) String authCode) {

        if (authCode != null) {
            log.debug("Received authCode: " + authCode);
            HsbcUserAccessToken.setCurrentToken(hsbcAuth.getAccessToken(authCode));
            return "redirect:/dashboard";
        }

        log.info("Redirecting to Quickstats controller");
        return "redirect:/quickstats";
    }
}
