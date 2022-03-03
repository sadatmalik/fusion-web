package com.sadatmalik.fusionweb.mappers;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.MonthlyIncome;
import com.sadatmalik.fusionweb.model.WeeklyExpense;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;

public class IncomeExpenseMapper {

    public static MonthlyExpenseDto monthlyExpenseToMonthlyExpenseDto(MonthlyExpense monthlyExpense) {
        return MonthlyExpenseDto.builder()
                .id(monthlyExpense.getId())
                .name(monthlyExpense.getName())
                .amount(monthlyExpense.getAmount().doubleValue())
                .dayOfMonthPaid(monthlyExpense.getDayOfMonthPaid())
                .type(monthlyExpense.getType())
                .accountId(monthlyExpense.getAccount().getAccountId())
                .build();
    }

    public static WeeklyExpenseDto weeklyExpenseToWeeklyExpenseDto(WeeklyExpense weeklyExpense) {
        return WeeklyExpenseDto.builder()
                .id(weeklyExpense.getId())
                .name(weeklyExpense.getName())
                .amount(weeklyExpense.getAmount().doubleValue())
                .timesPerWeek(weeklyExpense.getTimesPerWeek())
                .weeklyInterval(weeklyExpense.getWeeklyInterval())
                .type(weeklyExpense.getType())
                .accountId(weeklyExpense.getAccount().getAccountId())
                .build();
    }

    public static MonthlyIncomeDto monthlyIncomeToMonthlyIncomeDto(MonthlyIncome monthlyIncome) {
        return MonthlyIncomeDto.builder()
                .id(monthlyIncome.getId())
                .source(monthlyIncome.getSource())
                .amount(monthlyIncome.getAmount().doubleValue())
                .dayOfMonthReceived(monthlyIncome.getDayOfMonthReceived())
                .accountId(monthlyIncome.getAccount().getAccountId())
                .build();
    }
}
