package com.sadatmalik.fusionweb.model;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Data
public class WeeklyIncomeDto {
    @Nullable
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String source;

    @Positive
    @NotNull
    private Double amount;

    @NotNull
    @Min(1)
    @Max(31)
    private int weeklyInterval;

    @NotBlank
    @Size(max = 255)
    private String accountId;
}
