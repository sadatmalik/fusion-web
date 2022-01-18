package com.sadatmalik.fusionweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HsbcAmount {
    @JsonProperty("Amount")
    private Double amount;

    @JsonProperty("Currency")
    private String currency;
}