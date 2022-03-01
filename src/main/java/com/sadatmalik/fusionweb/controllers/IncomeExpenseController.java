package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IncomeExpenseController {

    private final ExpenseService expenseService;

    @GetMapping({"/income-and-expenses"})
    public String incomeAndExpenses(Model model) {
        log.info("Returning income and expenses page");

        MonthlyExpenseDto monthlyExpense = new MonthlyExpenseDto();
        model.addAttribute("monthlyExpense", monthlyExpense);

        return "income-and-expenses";
    }

//    @GetMapping({"/income-and-expenses/new-monthly-expense"})
//    public String newMonthlyExpense(Model model) {
//        log.info("Returning create new monthly expense page");
//        MonthlyExpense monthlyExpense = new MonthlyExpense();
//        model.addAttribute("monthlyExpense", monthlyExpense);
//        return "new-monthly-expense";
//    }

    @PostMapping("/income-and-expenses/new-monthly-expense")
    public String save(@ModelAttribute("monthlyExpense") MonthlyExpenseDto monthlyExpense, Model model) {
        // @todo setup field validations for new expense
        try {
            log.debug("Saving new monthly expense - " + monthlyExpense);
            //expenseService.saveExpense(monthlyExpense);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "redirect:/income-and-expenses";
    }
}
