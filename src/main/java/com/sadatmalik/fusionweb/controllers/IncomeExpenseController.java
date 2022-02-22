package com.sadatmalik.fusionweb.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IncomeExpenseController {

    @GetMapping({"", "/income-and-expenses"})
    public String home() {
        log.info("Returning income and expenses page");
        return "income-and-expenses";
    }

}
