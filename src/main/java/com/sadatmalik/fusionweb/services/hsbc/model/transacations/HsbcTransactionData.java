package com.sadatmalik.fusionweb.services.hsbc.model.transacations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Object representation used in the mapping of the OB API Json response for
 * Hsbc transaction data.
 *
 * @author sadatmalik
 */
@Getter
@Setter
@ToString
public class HsbcTransactionData {

    // Transactions for AccountID ---------{"Data":
    // {"Transaction":[{"AccountId":"GB0012345678",
    //                  "TransactionId":"00857cb6-64f9-4499-9624-ee6ed4a96cac",
    //                  "CreditDebitIndicator":"Credit",
    //                  "Status":"Booked",
    //                  "BookingDateTime":"2018-11-29T18:12:03Z",
    //                  "Amount":{"Amount":"1815.70000","Currency":"GBP"}},
    //                 {"AccountId":"GB0012345678","TransactionId":"6fd50b50-424a-4785-9145-14e24d70f1a7","CreditDebitIndicator":"Credit","Status":"Booked","BookingDateTime":"2018-12-03T21:17:09Z","Amount":{"Amount":"415.70000","Currency":"GBP"}},
    //                 {"AccountId":"GB0012345678","TransactionId":"e9cadb49-ecf2-4498-b423-78b98897652a","CreditDebitIndicator":"Credit","Status":"Pending","BookingDateTime":"2018-12-07T13:42:03Z","Amount":{"Amount":"15.80000","Currency":"GBP"}}]},
    //
    //                 "Links":{"Self":"https://sandbox.hsbc.com/psd2/obie/v3.1/obie/v3.1/accounts/GB0012345678/transactions"},"Meta":{}}
    @JsonProperty("Transaction")
    private List<HsbcTransaction> transactionList;

}
