package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.services.HsbcAccessToken;
import com.sadatmalik.fusionweb.services.HsbcConsent;
import com.sadatmalik.fusionweb.services.HsbcService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    HsbcService hsbc;

    @GetMapping({"", "/"})
    public String home() {
        logger.info("Returning index page");
        return "index";
    }

    @GetMapping("/dashboard")
    public String error() {
        HsbcAccessToken accessToken = hsbc.getAccessToken();
        HsbcConsent consent = hsbc.getConsentID(accessToken);

        String authorizationURL = hsbc.getAuthorizationURL(consent);
        System.out.println(authorizationURL);

        //return "redirect:" + authorizationURL;
        return "dashboard";
    }
}
