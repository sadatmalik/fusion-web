package com.sadatmalik.fusionweb.model.dto;

import com.sadatmalik.fusionweb.model.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyExpenseDto {
    @Nullable
    private Long id;

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
