package com.sadatmalik.fusionweb.model.dto;

import com.sadatmalik.fusionweb.model.ExpenseType;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class MonthlyExpenseDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Positive
    @NotNull
    private Double amount;

    @NotNull
    @Min(1)
    @Max(31)
    private int dayOfMonthPaid;

    @NotNull
    private ExpenseType type;

    @NotBlank
    @Size(max = 255)
    private String accountId;
}
