package com.sadatmalik.fusionweb.services.hsbc.model.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sadatmalik.fusionweb.model.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HsbcAccount {

    // @todo hardcoded - use an enum? tie it to constructor?
    private String bank = "HSBC";

    @JsonProperty("AccountId")
    private String accountId;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("AccountType")
    private String type;

    @JsonProperty("AccountSubType")
    private String subType;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Account")
    private List<HsbcAccountInfo> accountInfo;

    @JsonIgnore
    private Balance balance;
}
