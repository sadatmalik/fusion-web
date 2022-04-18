package com.sadatmalik.fusionweb.services.hsbc.model.transacations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Object representation used in the mapping of the OB API Json response for
 * Hsbc transaction data.
 *
 * @author sadatmalik
 */
@Getter
@Setter
@ToString
public class HsbcTransactionObject {

    @JsonProperty("Data")
    private HsbcTransactionData data;

}
