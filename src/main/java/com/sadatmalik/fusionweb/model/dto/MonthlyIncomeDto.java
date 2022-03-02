package com.sadatmalik.fusionweb.model.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Data
public class MonthlyIncomeDto {
    @Nullable
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String source;

    @Positive
    @NotNull
    private Double amount;

    @NotBlank
    @Min(1)
    @Max(31)
    private int dayOfMonthReceived;

    @NotBlank
    @Size(max = 255)
    private String accountId;
}
