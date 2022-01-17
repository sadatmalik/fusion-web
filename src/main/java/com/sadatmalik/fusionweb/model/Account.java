package com.sadatmalik.fusionweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

// @todo make this a JPA entity
@ToString
public class Account {
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
}
