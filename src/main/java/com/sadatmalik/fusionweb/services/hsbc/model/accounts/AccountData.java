package com.sadatmalik.fusionweb.services.hsbc.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AccountData {
    @JsonProperty("Account")
    private List<Account> accounts;
}
