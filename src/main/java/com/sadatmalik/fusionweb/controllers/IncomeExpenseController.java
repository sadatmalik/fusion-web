package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.MonthlyIncome;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.WeeklyExpense;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.IncomeExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IncomeExpenseController {

    private final IncomeExpenseService incomeExpenseService;

    @GetMapping({"/income-and-expenses"})
    public String incomeAndExpenses(Authentication authentication, Model model) {
        log.info("Returning income and expenses page");

        model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
        model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
        model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());

        loadTableData(authentication, model);

        return "income-and-expenses";
    }

    private void loadTableData(Authentication authentication, Model model) {
        model.addAttribute("monthlyExpenseList", getMonthlyExpenses(authentication));
        model.addAttribute("weeklyExpenseList", getWeeklyExpenses(authentication));
        model.addAttribute("monthlyIncomeList", getMonthlyIncome(authentication));
    }

    private List<MonthlyExpense> getMonthlyExpenses(Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        // @todo convert to Dto list?
        return incomeExpenseService.getMonthlyExpensesFor(user);
    }

    private List<WeeklyExpense> getWeeklyExpenses(Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        // @todo convert to Dto list?
        return incomeExpenseService.getWeeklyExpensesFor(user);
    }

    private List<MonthlyIncome> getMonthlyIncome(Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        // @todo convert to Dto list?
        return incomeExpenseService.getMonthlyIncomeFor(user);
    }

    @PostMapping("/income-and-expenses/new-monthly-expense")
    public String saveMonthlyExpense(@ModelAttribute("monthlyExpenseDto") @Valid MonthlyExpenseDto monthlyExpenseDto,
                       BindingResult bindingResult,
                       Authentication authentication,
                       Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - MonthlyExpenseDto has validation errors");
            model.addAttribute("showNewMonthlyExpenseForm", true);

            model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());

            model.addAttribute("monthlyExpenseList", getMonthlyExpenses(authentication));
            model.addAttribute("weeklyExpenseList", getWeeklyExpenses(authentication));
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new monthly expense - " + monthlyExpenseDto);
            MonthlyExpense saved = incomeExpenseService.saveMonthlyExpense(monthlyExpenseDto, Utils.getUser(authentication));
            log.debug("Saved new monthly expense to DB - " + saved);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    @PostMapping("/income-and-expenses/new-weekly-expense")
    public String saveWeeklyExpense(@ModelAttribute("weeklyExpenseDto") @Valid WeeklyExpenseDto weeklyExpenseDto,
                       BindingResult bindingResult,
                       Authentication authentication,
                       Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - WeeklyExpenseDto has validation errors");
            model.addAttribute("showNewWeeklyExpenseForm", true);

            model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());

            model.addAttribute("monthlyExpenseList", getMonthlyExpenses(authentication));
            model.addAttribute("weeklyExpenseList", getWeeklyExpenses(authentication));
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new weekly expense - " + weeklyExpenseDto);
            WeeklyExpense saved = incomeExpenseService.saveWeeklyExpense(weeklyExpenseDto, Utils.getUser(authentication));
            log.debug("Saved new monthly expense to DB - " + saved);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }
}
