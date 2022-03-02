package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.Debt;
import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.AccountService;
import com.sadatmalik.fusionweb.services.DebtService;
import com.sadatmalik.fusionweb.services.IncomeExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuickStatsController {

    private final AccountService accountService;
    private final IncomeExpenseService incomeExpenseService;
    private final DebtService debtService;

    @GetMapping("/quickstats")
    public String quickStats(Authentication authentication, Model model) {
        log.info("Returning Quickstats page");

        // get logged-in users accounts
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        List<Account> accounts = accountService.getAccountsFor(user);

        // total balance
        model.addAttribute("totalBalance", Utils.getTotalDisplayBalance(accounts));
        // @todo hardcoded balance chart data
        model.addAttribute("chartData", getChartData());

        // payments
        List<MonthlyExpense> paymentsList = incomeExpenseService.getMonthlyExpensesFor(user);
        List<List<Object>> paymentChartData = getPaymentChartData(paymentsList);
        model.addAttribute("paymentChartData", paymentChartData);

        // debts
        List<Debt> debtList = debtService.getDebtFor(user);
        List<List<Object>> debtChartData = getDebtChartData(debtList);
        model.addAttribute("debtChartData", debtChartData);

        model.addAttribute("date",
                LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM, dd yyyy")));

        return "quickstats";
    }

    private List<List<Object>> getPaymentChartData(List<MonthlyExpense> paymentsList) {
        List<List<Object>> paymentChartData = new ArrayList<>(List.of(List.of("TYPE", "To pay:")));
        paymentsList.stream()
                .forEach(expense ->
                        paymentChartData.add(List.of(expense.getName(), expense.getAmount()))
                );
        return paymentChartData;
    }

    private List<List<Object>> getDebtChartData(List<Debt> debtList) {
        List<List<Object>> debtChartData = new ArrayList<>(List.of(List.of("Lender", "Borrowing")));
        debtList.stream()
                .forEach(debt ->
                    debtChartData.add(List.of(debt.getLender(), debt.getTotalBorrowed()))
                );
        return debtChartData;
    }

    private List<List<Object>> getChartData() {
        return List.of(
                List.of("Date", "Balance"),
                List.of("25-Jan", 5201.24),
                List.of("26-Jan", 3580.02),
                List.of("27-Jan", 3580.02),
                List.of("28-Jan", 3580.02),
                List.of("29-Jan", 3500.23),
                List.of("30-Jan", 3500.23),
                List.of("31-Jan", 3500.23),
                List.of("1-Feb", 3500.23),
                List.of("2-Feb", 3500.23),
                List.of("3-Feb", 3500.23),
                List.of("4-Feb", 3500.23),
                List.of("5-Feb", 3500.23),
                List.of("6-Feb", 3500.23),
                List.of("7-Feb", 3456.12),
                List.of("8-Feb", 3456.12),
                List.of("9-Feb", 2967.13),
                List.of("10-Feb", 2967.13),
                List.of("11-Feb", 2967.13),
                List.of("12-Feb", 2967.13),
                List.of("13-Feb", 2967.13),
                List.of("14-Feb", 2967.13),
                List.of("15-Feb", 2967.13),
                List.of("16-Feb", 2967.13),
                List.of("17-Feb", 2879.32),
                List.of("18-Feb", 2879.32),
                List.of("19-Feb", 2879.32),
                List.of("20-Feb", 2879.32),
                List.of("22-Feb", 2700.10),
                List.of("23-Feb", 2700.10),
                List.of("24-Feb", 2247.20)
        );
    }
}
