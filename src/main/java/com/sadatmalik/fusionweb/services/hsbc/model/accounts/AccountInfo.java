package com.sadatmalik.fusionweb.services.hsbc.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountInfo {
    @JsonProperty("SchemeName")
    private String scheme; // @todo make this an enum

    @JsonProperty("Identification")
    private String identifier;

    @JsonProperty("Name")
    private String name;
}
