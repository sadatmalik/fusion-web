package com.sadatmalik.fusionweb.mappers;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;

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
}
