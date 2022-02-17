package com.sadatmalik.fusionweb.services.hsbc.model.balances;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HsbcBalance {
    @JsonProperty("AccountId")
    private String accountId;

    @JsonProperty("CreditDebitIndicator")
    private String creditDebit;  // @todo make this an enum?

    @JsonProperty("Amount")
    private HsbcAmount amount;  // @todo make this an enum?
}