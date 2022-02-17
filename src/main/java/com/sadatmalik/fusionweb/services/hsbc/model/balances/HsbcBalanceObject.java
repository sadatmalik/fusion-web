package com.sadatmalik.fusionweb.services.hsbc.model.balances;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class HsbcBalanceObject {
    @JsonProperty("Data")
    private HsbcBalanceData data;
}
