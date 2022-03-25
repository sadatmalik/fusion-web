package com.sadatmalik.fusionweb.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Spring MVC controller that handles all endpoints related to the addition of
 * new user accounts.
 *
 * The idea is to manage all endpoints related to all downstream instituions -
 * i.e. an endpoint per provider of the Open Banking API that Fusion will
 * be connecting with.
 *
 * When the end user wants to connect an account for a specific organisation,
 * they will be forwarded to that organisations landing page, with details of
 * the offering, which the user can review before proceeding to Oauth
 * account authentication.
 *
 * @author sadatmalik
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AddAccountController {

    /**
     * Handles requests for the application endpoint "add-account".
     *
     * @return returns the addaccount view
     */
    @GetMapping({"/add-account"})
    public String addAccount() {
        log.info("Returning add accounts page");
        return "addaccount";
    }

    /**
     * Handles requests for the application endpoint "add-account/hsbc".
     * The idea is to have an endpoint like this per connected end organisation.
     * I.e. one per Open Banking API.
     *
     * @return returns the addaccount-hsbc view
     */
    @GetMapping({"/add-account/hsbc"})
    public String hsbc() {
        log.info("Returning add hsbc account page");
        return "addaccount-hsbc";
    }

}
