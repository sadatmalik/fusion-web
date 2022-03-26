package com.sadatmalik.fusionweb.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Spring MVC controller class for the /login endpoint.
 *
 * Overrides the default login form provided by Spring, to match the look and
 * feel of the web application templates.
 *
 * @author sadatmalik
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    /**
     * Handles /login endpoint requests, returning a custom login view.
     *
     * @return returns custom login view.
     */
    @GetMapping({"/login"})
    public String login() {
        log.info("Returning login page");
        return "login";
    }

}
