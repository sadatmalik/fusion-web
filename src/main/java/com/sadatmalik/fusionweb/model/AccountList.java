package com.sadatmalik.fusionweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class AccountList {
    @JsonProperty("Data")
    private AccountData data;

}