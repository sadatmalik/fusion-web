package com.sadatmalik.fusionweb.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AddAccountController {

    @GetMapping({"/add-account"})
    public String addAccount() {
        log.info("Returning add accounts page");
        return "addaccount";
    }

    @GetMapping({"/add-account/hsbc"})
    public String hsbc() {
        log.info("Returning add hsbc account page");
        return "addaccount-hsbc";
    }

}
