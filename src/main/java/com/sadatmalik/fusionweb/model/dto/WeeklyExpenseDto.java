package com.sadatmalik.fusionweb.model.dto;

import com.sadatmalik.fusionweb.model.ExpenseType;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Data
public class WeeklyExpenseDto {

    @Nullable
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Positive
    @NotNull
    private Double amount;

    @Nullable
    @Min(1)
    @Max(7)
    private int timesPerWeek;

    @Nullable
    @Min(1)
    @Max(4)
    private int weeklyInterval;

    @NotNull
    private ExpenseType type;

    @NotBlank
    @Size(max = 255)
    private String accountId;

}
