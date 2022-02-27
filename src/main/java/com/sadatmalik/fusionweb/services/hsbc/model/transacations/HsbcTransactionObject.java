package com.sadatmalik.fusionweb.services.hsbc.model.transacations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HsbcTransactionObject {

    @JsonProperty("Data")
    private HsbcTransactionData data;

}
