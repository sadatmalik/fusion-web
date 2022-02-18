package com.sadatmalik.fusionweb.bootstrap;

import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Slf4j
@Profile("dev")
@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final MonthlyIncomeRepository monthlyIncomeRepository;
    private final DebtRepository debtRepository;
    private final WeeklyExpenseRepository weeklyExpenseRepository;
    private final MonthlyExpenseRepository monthlyExpenseRepository;

    @Override
    public void run(String... args) throws Exception {
        // set up a test user
        log.debug("Setting up Bootstrap user");

        User user = User.builder()
                .firstName("Sadat")
                .lastName("Malik")
                .email("sadat.malik@me.com")
                .build();

        userRepository.save(user);

        // set up some test accounts
        log.debug("Setting up Bootstrap accounts");

        Account account = Account.builder()
                .accountId("HS345678")
                .type(AccountType.CURRENT)
                .name("HSBC")
                .balance(20000)
                .user(user)
                .build();

        Account account2 = Account.builder()
                .accountId("BA123456")
                .type(AccountType.SAVINGS)
                .name("BARC")
                .balance(65000)
                .user(user)
                .build();

        user.setAccounts(List.of(account, account2));

        accountRepository.save(account);
        accountRepository.save(account2);

        // set up some income data
        log.debug("Setting up Bootstrap income");

        Income income = Income.builder()
                .account(account)
                .user(user)
                .amount(new BigDecimal(35))
                .source("Job")
                .weeklyInterval(0)
                .build();

        account.setIncomeList(List.of(income));
        user.setIncomeList(List.of(income));
        incomeRepository.save(income);

        // set up some income data
        log.debug("Setting up Bootstrap monthly income");

        MonthlyIncome monthly = MonthlyIncome.builder()
                .account(account)
                .user(user)
                .amount(new BigDecimal(1850))
                .source("Rent")
                .dayOfMonthReceived(7)
                .build();

        account.setMonthlyIncomeList(List.of(monthly));
        user.setMonthlyIncomeList(List.of(monthly));
        monthlyIncomeRepository.save(monthly);

        // set up some debt data
        log.debug("Setting up Bootstrap debt");

        Debt debt = Debt.builder()
                .account(account)
                .user(user)
                .lender("Santander")
                .totalBorrowed(new BigDecimal(350000))
                .totalOwed(new BigDecimal(350000))
                .dayOfMonthPaid(5)
                .interestRate(new BigDecimal(1.74))
                .dateBorrowed(new Date(119,1,1)) //2019-Feb-1st
                .initialTermMonths(12*25)
                .build();

        account.setDebts(List.of(debt));
        user.setDebts(List.of(debt));
        debtRepository.save(debt);

        // set up some expense data
        log.debug("Setting up Bootstrap expenses");

        WeeklyExpense weeklyExpense = WeeklyExpense.builder()
                .account(account)
                .user(user)
                .name("Sainsburys")
                .amount(new BigDecimal(80))
                .timesPerWeek(1)
                .weeklyInterval(1)
                .type(ExpenseType.GROCERIES)
                .build();

        account.setWeeklyExpenses(List.of(weeklyExpense));
        user.setWeeklyExpenses(List.of(weeklyExpense));
        weeklyExpenseRepository.save(weeklyExpense);

        MonthlyExpense monthlyExpense = MonthlyExpense.builder()
                .account(account)
                .user(user)
                .name("Scottish Power")
                .amount(new BigDecimal(140))
                .dayOfMonthPaid(18)
                .type(ExpenseType.BILL)
                .build();

        account.setMonthlyExpenses(List.of(monthlyExpense));
        user.setMonthlyExpenses(List.of(monthlyExpense));
        monthlyExpenseRepository.save(monthlyExpense);
    }
}
