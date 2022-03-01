package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.AccountService;
import com.sadatmalik.fusionweb.services.ExpenseService;
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
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IncomeExpenseController {

    private final AccountService accountService;
    private final ExpenseService expenseService;

    @GetMapping({"/income-and-expenses"})
    public String incomeAndExpenses(Authentication authentication, Model model) {
        log.info("Returning income and expenses page");

        MonthlyExpenseDto monthlyExpense = new MonthlyExpenseDto();
        model.addAttribute("monthlyExpense", monthlyExpense);

        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        List<MonthlyExpense> monthlyExpenseList = expenseService.getMonthlyExpensesFor(user);
        model.addAttribute("monthlyExpenseList", monthlyExpenseList);

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
    public String save(@ModelAttribute("monthlyExpense") @Valid MonthlyExpenseDto monthlyExpenseDto,
                       BindingResult bindingResult,
                       Authentication authentication,
                       Model model) {
        // @todo setup field validations for new expense
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on received monthlyExpenseDto");
            model.addAttribute("collapseShow", true);
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new monthly expense - " + monthlyExpenseDto);

            User user = ((UserPrincipal) authentication.getPrincipal()).getUser();

            // @todo use MapStruct
            MonthlyExpense monthlyExpense = MonthlyExpense.builder()
                    .name(monthlyExpenseDto.getName())
                    .amount(new BigDecimal(monthlyExpenseDto.getAmount()))
                    .dayOfMonthPaid(monthlyExpenseDto.getDayOfMonthPaid())
                    .type(monthlyExpenseDto.getType())
                    .account(accountService.getAccountByAccountId(
                            monthlyExpenseDto.getAccountId())
                    )
                    .user(user)
                    .build();

            MonthlyExpense saved = expenseService.saveExpense(monthlyExpense);

            log.debug("Saved new monthly expense to DB - " + saved);

        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "redirect:/income-and-expenses";
    }
}
