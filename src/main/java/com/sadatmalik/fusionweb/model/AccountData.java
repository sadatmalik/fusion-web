package com.sadatmalik.fusionweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.List;

@ToString
public class AccountData {
    @JsonProperty("Account")
    private List<Account> accounts;
}
