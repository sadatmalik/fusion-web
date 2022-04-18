package com.sadatmalik.fusionweb.services.hsbc.model.transacations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sadatmalik.fusionweb.services.hsbc.model.balances.HsbcAmount;
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
public class HsbcTransaction {
    // {"AccountId":"GB0012345678",
    @JsonProperty("AccountId")
    private String accountId;

    //  "TransactionId":"00857cb6-64f9-4499-9624-ee6ed4a96cac",
    @JsonProperty("TransactionId")
    private String transactionId;

    //  "CreditDebitIndicator":"Credit",
    @JsonProperty("CreditDebitIndicator")
    private String creditDebitIndicator;

    //  "Status":"Booked",
    @JsonProperty("Status")
    private String status;

    //  "BookingDateTime":"2018-11-29T18:12:03Z",
    @JsonProperty("BookingDateTime")
    private String bookingDateTime;

    //  "Amount":{"Amount":"1815.70000","Currency":"GBP"}
    @JsonProperty("Amount")
    private HsbcAmount hsbcAmount;

}
