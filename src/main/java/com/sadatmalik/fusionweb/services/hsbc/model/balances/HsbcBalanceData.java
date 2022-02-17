package com.sadatmalik.fusionweb.services.hsbc.model.balances;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class HsbcBalanceData {
    @JsonProperty("Balance")
    private List<HsbcBalance> balanceList;
}