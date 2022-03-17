package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.ExpenseType;
import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;

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
}
