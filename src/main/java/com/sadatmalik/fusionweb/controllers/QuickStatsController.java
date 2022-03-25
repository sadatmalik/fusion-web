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

/**
 * Spring MVC controller class for QuickStats view related endpoints.
 *
 * Several methods, for example {@code quickStats(Authentication authentication,
 * Model model)} return chart data in the MVC model. Note that the current
 * implementation uses Google Charts for the rendering of chart graphics in the
 * HTML application views.
 *
 * The return type of data for rendering using Google forms is: {@code
 * List<List<Object>>}
 *
 * The controller is intended as the handler for future release endpoints that
 * will implement enhanced charts and analytics information.
 *
 * @author sadatmalik
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class QuickStatsController {

    private final AccountService accountService;
    private final IncomeExpenseService incomeExpenseService;
    private final DebtService debtService;

    /**
     * Handles /quickstats endpoint GET requests. It is the primary method of the
     * controller class.
     *
     * Retrieves account infomration from the {@code AccountService} instance for
     * the user corresponding to the current authentication. The total balance for
     * all user accounts is calculated and returned in the MVC model as a formatted
     * String.
     *
     * Retrieves monthly expense data from the {@code IncomeExpenseService} instance
     * for the current user, and translates this into the charting format required
     * by Google Charts.
     *
     * Similarly, for debt data, retrieves the details for the current user from the
     * {@code DebtService instance} and translates this into a data format that can
     * be rendered by Google Charts.
     *
     * @param authentication provided by the Spring context.
     * @param model injected by the Spring context.
     * @return returns the quickstats view.
     */
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
        paymentsList
                .forEach(expense ->
                        paymentChartData.add(List.of(expense.getName(), expense.getAmount()))
                );
        return paymentChartData;
    }

    private List<List<Object>> getDebtChartData(List<Debt> debtList) {
        List<List<Object>> debtChartData = new ArrayList<>(List.of(List.of("Lender", "Borrowing")));
        debtList
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
