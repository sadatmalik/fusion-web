package com.sadatmalik.fusionweb.model.dto;

import com.sadatmalik.fusionweb.model.ExpenseType;
import lombok.Data;

@Data
public class MonthlyExpenseDto {
    private String name;
    private Double amount;
    private int dayOfMonthPaid;
    private ExpenseType type;
    private String accountId;
}
