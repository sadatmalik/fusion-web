package com.sadatmalik.fusionweb.services.hsbc.model.balances;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
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
@Builder
public class HsbcBalance {
    @JsonProperty("AccountId")
    private String accountId;

    @JsonProperty("CreditDebitIndicator")
    private String creditDebit;  // @todo make this an enum?

    @JsonProperty("Amount")
    private HsbcAmount amount;  // @todo make this an enum?
}