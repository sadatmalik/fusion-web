package com.sadatmalik.fusionweb.services.hsbc.model.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sadatmalik.fusionweb.services.hsbc.model.balances.Balance;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

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
    private List<AccountInfo> accountInfo;

    @JsonIgnore
    private Balance balance;
}
