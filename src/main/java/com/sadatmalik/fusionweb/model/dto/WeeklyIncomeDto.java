package com.sadatmalik.fusionweb.model.dto;

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
    @Max(4)
    private int weeklyInterval;

    @NotBlank
    @Size(max = 255)
    private String accountId;
}