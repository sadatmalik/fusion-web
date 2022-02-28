package com.sadatmalik.fusionweb.bootstrap;

import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.model.websecurity.Authority;
import com.sadatmalik.fusionweb.model.websecurity.RoleEnum;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.repositories.*;
import com.sadatmalik.fusionweb.repositories.websecurity.AuthorityRepo;
import com.sadatmalik.fusionweb.repositories.websecurity.UserPrincipalRepo;
import com.sadatmalik.fusionweb.services.AccountServicesRegistry;
import com.sadatmalik.fusionweb.services.DummyBarclaysSavingsTransactionService;
import com.sadatmalik.fusionweb.services.DummyHsbcSavingsTransactionService;
import com.sadatmalik.fusionweb.services.DummyTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;

@Slf4j
@Profile("dev")
@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {

    // application model
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final MonthlyIncomeRepository monthlyIncomeRepository;
    private final DebtRepository debtRepository;
    private final WeeklyExpenseRepository weeklyExpenseRepository;
    private final MonthlyExpenseRepository monthlyExpenseRepository;
    private final GoalRepository goalRepository;


    // web security
    private final AuthorityRepo authorityRepo;
    private final UserPrincipalRepo userPrincipalRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // set up a test user
        log.debug("Setting up Bootstrap user");

        User user = User.builder()
                .firstName("Sadat")
                .lastName("Malik")
                .email("sadat.malik@me.com")
                .build();

        User savedUser = userRepository.save(user);

        // bootstrap web security
        createDummyUserAndAuthority(savedUser);

        // set up some banks
        Bank hsbc = bankRepository.save(Bank.builder()
                .name("HSBC")
                .imageLocation("/images/hsbc.png")
                .build()
        );

        Bank barclays = bankRepository.save(Bank.builder()
                .name("Barclays")
                .imageLocation("/images/barclays-logo.png")
                .build()
        );

        bankRepository.save(hsbc);

        // set up some test accounts
        log.debug("Setting up Bootstrap accounts");

        Account account = accountRepository.save(Account.builder()
                .accountId("HS000345678")
                .type(AccountType.SAVINGS)
                .name("HSBC")
                .balance(14227.39)
                .currency("GBP")
                .user(user)
                .description("HSBC Savings account")
                .bank(hsbc)
                .build()
        );

        Account account2 = accountRepository.save(Account.builder()
                .accountId("BARC0003456")
                .type(AccountType.SAVINGS)
                .name("BARC")
                .balance(41075.29)
                .currency("GBP")
                .user(user)
                .description("Barclays Everyday Saver account")
                .bank(barclays)
                .build()
        );


        Account account3 = accountRepository.save(Account.builder()
                .accountId("BARC0007891")
                .type(AccountType.CURRENT)
                .name("BARC")
                .balance(1268.08)
                .currency("GBP")
                .user(user)
                .description("Barclays Cash OD account")
                .bank(barclays)
                .build()
        );

        user.setAccounts(List.of(account, account2, account3));

        // Set Transaction Service for dummy accounts
        AccountServicesRegistry.getInstance().registerTransactionService(account,
                new DummyHsbcSavingsTransactionService());
        AccountServicesRegistry.getInstance().registerTransactionService(account2,
                new DummyBarclaysSavingsTransactionService());
        AccountServicesRegistry.getInstance().registerTransactionService(account3,
                new DummyTransactionService());

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
                .totalBorrowed(new BigDecimal(295000))
                .totalOwed(new BigDecimal(270000))
                .dayOfMonthPaid(5)
                .interestRate(new BigDecimal(1.95))
                .dateBorrowed(new Date(119,1,1)) //2019-Feb-1st
                .initialTermMonths(12*25)
                .build();

        Debt debt2 = Debt.builder()
                .account(account)
                .user(user)
                .lender("Nationwide")
                .totalBorrowed(new BigDecimal(400000))
                .totalOwed(new BigDecimal(400000))
                .dayOfMonthPaid(5)
                .interestRate(new BigDecimal(2.34))
                .dateBorrowed(new Date(119,1,1)) //2019-Feb-1st
                .initialTermMonths(12*25)
                .build();

        account.setDebts(List.of(debt, debt2));
        user.setDebts(List.of(debt, debt2));

        debtRepository.save(debt);
        debtRepository.save(debt2);

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

        // set up some goal data
        log.debug("Setting up Bootstrap expenses");

        Goal goal = Goal.builder()
                .account(account)
                .user(user)
                .name("New Laptop")
                .target(new BigDecimal(1500))
                .achieved(new BigDecimal(0))
                .currentContribution(new BigDecimal(200))
                .contribWeekly(false) // monthly contribtuion to goal
                .build();

        account.setGoals(List.of(goal));
        user.setGoals(List.of(goal));
        goalRepository.save(goal);

    }

    private void createDummyUserAndAuthority(User user) {
        Authority userAuth = Authority.builder().authority(RoleEnum.ROLE_USER).build();
        if (authorityRepo.findAll().isEmpty()) {
            authorityRepo.save(userAuth);
        }

        if (userPrincipalRepo.findAll().isEmpty()) {
            UserPrincipal principal = new UserPrincipal("USER",
                    passwordEncoder.encode("user"),
                    Collections.singletonList(userAuth));

            principal.setUser(user);

            userPrincipalRepo.save(principal);
        }
    }

}
