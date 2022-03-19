package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyIncomeDto;

import java.math.BigDecimal;

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
}
