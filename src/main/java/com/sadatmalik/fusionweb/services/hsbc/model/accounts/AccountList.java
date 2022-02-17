package com.sadatmalik.fusionweb.services.hsbc.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountList {
    @JsonProperty("Data")
    private AccountData data;

}