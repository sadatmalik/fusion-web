package com.sadatmalik.fusionweb.services.hsbc.model.balances;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Object representation used in the mapping of the OB API Json response for
 * Hsbc balance data.
 *
 * @author sadatmalik
 */
@Getter
@Setter
@ToString
public class HsbcBalanceObject {
    @JsonProperty("Data")
    private HsbcBalanceData data;
}
