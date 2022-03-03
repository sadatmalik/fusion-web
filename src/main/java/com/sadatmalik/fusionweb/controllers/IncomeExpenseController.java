package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.mappers.IncomeExpenseMapper;
import com.sadatmalik.fusionweb.model.*;
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
import org.springframework.web.bind.annotation.PathVariable;
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

        loadFormBindingObjects(model);
        loadTableData(authentication, model);

        return "income-and-expenses";
    }

    private void loadFormBindingObjects(Model model) {
        model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
        model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
        model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());
        model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());
    }

    private void loadTableData(Authentication authentication, Model model) {
        model.addAttribute("monthlyExpenseList", getMonthlyExpenses(authentication));
        model.addAttribute("weeklyExpenseList", getWeeklyExpenses(authentication));
        model.addAttribute("monthlyIncomeList", getMonthlyIncome(authentication));
        model.addAttribute("weeklyIncomeList", getWeeklyIncome(authentication));
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

    private List<Income> getWeeklyIncome(Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        // @todo convert to Dto list?
        return incomeExpenseService.getWeeklyIncomeFor(user);
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
            model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());
            model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());

            loadTableData(authentication, model);
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
            model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());
            model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());

            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new weekly expense - " + weeklyExpenseDto);
            WeeklyExpense saved = incomeExpenseService.saveWeeklyExpense(weeklyExpenseDto, Utils.getUser(authentication));
            log.debug("Saved new weekly expense to DB - " + saved);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    @PostMapping("/income-and-expenses/new-monthly-income")
    public String saveMonthlyIncome(@ModelAttribute("monthlyIncomeDto") @Valid MonthlyIncomeDto monthlyIncomeDto,
                                    BindingResult bindingResult,
                                    Authentication authentication,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - MonthlyIncomeDto has validation errors");
            model.addAttribute("showNewMonthlyIncomeForm", true);

            model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
            model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
            model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());

            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new monthly income - " + monthlyIncomeDto);
            MonthlyIncome saved = incomeExpenseService.saveMonthlyIncome(monthlyIncomeDto, Utils.getUser(authentication));
            log.debug("Saved new monthly income to DB - " + saved);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    @PostMapping("/income-and-expenses/new-weekly-income")
    public String saveWeeklyIncome(@ModelAttribute("weeklyIncomeDto") @Valid WeeklyIncomeDto weeklyIncomeDto,
                                    BindingResult bindingResult,
                                    Authentication authentication,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - WeeklyIncomeDto has validation errors");
            model.addAttribute("showNewWeeklyIncomeForm", true);

            model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
            model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
            model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());

            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new weekly income - " + weeklyIncomeDto);
            Income saved = incomeExpenseService.saveWeeklyIncome(weeklyIncomeDto, Utils.getUser(authentication));
            log.debug("Saved new weekly income to DB - " + saved);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    @GetMapping("/income-and-expenses/{expenseId}/view")
    public String viewMonthlyExpenseDetails(@PathVariable("expenseId") String id, Model model,
                                            Authentication authentication) {
        log.debug("View MonthlyExpense details");

        // retrieve the MonthlyExpense
        MonthlyExpense monthlyExpense = incomeExpenseService.getMonthlyExpenseFor(Long.parseLong(id));

        // set up the forms Dto
        model.addAttribute("editableMonthlyExpenseDto",
                IncomeExpenseMapper.monthlyExpenseToMonthlyExpenseDto(monthlyExpense));
        model.addAttribute("showMonthlyExpenseEditDeleteForm", true);

        model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
        model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
        model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());
        model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());

        loadTableData(authentication, model);
        return "income-and-expenses";
    }

    @GetMapping("/income-and-expenses/{expenseId}/delete")
    public String deleteMonthlyExpense(@PathVariable("expenseId") String id, Model model,
                                       Authentication authentication) {
        // delete the MonthlyExpense
        incomeExpenseService.deleteMonthlyExpenseFor(Long.parseLong(id));

        model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
        model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
        model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());
        model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());

        loadTableData(authentication, model);
        return "income-and-expenses";
    }
}
