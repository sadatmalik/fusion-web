package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyIncomeDto;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author sm@creativefusion.net
 */
public class TestUtils {

    static MonthlyExpenseDto mockMonthlyExpenseDto() {
        return MonthlyExpenseDto.builder()
                .name("BROADBAND")
                .amount(31.95)
                .dayOfMonthPaid(18)
                .type(ExpenseType.UTILITIES)
                .accountId("TEST")
                .build();
    }

    static MonthlyExpense mockMonthlyExpense() {
        return MonthlyExpense.builder()
                .id(1L)
                .user(new User())
                .name("BROADBAND")
                .amount(new BigDecimal(31.95))
                .dayOfMonthPaid(18)
                .type(ExpenseType.UTILITIES)
                .account(new Account())
                .build();
    }

    static WeeklyExpenseDto mockWeeklyExpenseDto() {
        return WeeklyExpenseDto.builder()
                .accountId("TEST")
                .name("Sainsburys")
                .amount(80d)
                .timesPerWeek(1)
                .weeklyInterval(1)
                .type(ExpenseType.GROCERIES)
                .build();
    }

    static WeeklyExpense mockWeeklyExpense() {
        return WeeklyExpense.builder()
                .account(new Account())
                .user(new User())
                .name("Sainsburys")
                .amount(new BigDecimal(80))
                .timesPerWeek(1)
                .weeklyInterval(1)
                .type(ExpenseType.GROCERIES)
                .build();
    }

    static MonthlyIncomeDto mockMonthlyIncomeDto() {
        return MonthlyIncomeDto.builder()
                .accountId("TEST")
                .amount(1850d)
                .source("Rent")
                .dayOfMonthReceived(7)
                .build();
    }

    static MonthlyIncome mockMonthlyIncome() {
        return MonthlyIncome.builder()
                .account(new Account())
                .user(new User())
                .amount(new BigDecimal(1850))
                .source("Rent")
                .dayOfMonthReceived(7)
                .build();
    }

    static WeeklyIncomeDto mockWeeklyIncomeDto() {
        return WeeklyIncomeDto.builder()
                .accountId("TEST")
                .amount(35d)
                .source("Job")
                .weeklyInterval(2)
                .build();
    }

    static Income mockWeeklyIncome() {
        return Income.builder()
                .account(new Account())
                .user(new User())
                .amount(new BigDecimal(35))
                .source("Job")
                .weeklyInterval(2)
                .build();
    }

    public static Account mockAccount() {
        Bank bank = Bank.builder()
                .name("HSBC")
                .imageLocation("/images/hsbc.png")
                .build();

        return Account.builder()
                .accountId("HS000345678")
                .type(AccountType.SAVINGS)
                .name("Mr Sadat Malik")
                .balance(1234.56)
                .currency("GBP")
                .user(new User())
                .description("HSBC Savings account")
                .bank(bank)
                .build();

    }

    public static User mockUser() {
        return User.builder()
                .firstName("Sadat")
                .lastName("Malik")
                .email("sm@creativefusion.net")
                .build();
    }

    public static Debt mockDebt() {
        return Debt.builder()
                .account(mockAccount())
                .user(mockUser())
                .lender("Santander")
                .totalBorrowed(new BigDecimal(295000))
                .totalOwed(new BigDecimal(270000))
                .dayOfMonthPaid(5)
                .interestRate(new BigDecimal(1.95))
                .dateBorrowed(new Date(119, 1, 1)) //2019-Feb-1st
                .initialTermMonths(12 * 25)
                .build();
    }

}
