package com.sadatmalik.fusionweb.oauth.hsbc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HsbcConsentData {
    //   {"Permissions":["ReadScheduledPaymentsDetail","ReadAccountsDetail","ReadStandingOrdersDetail","ReadStatementsBasic","ReadTransactionsCredits","ReadTransactionsDetail","ReadBalances","ReadParty","ReadScheduledPaymentsBasic","ReadStatementsDetail","ReadBeneficiariesDetail","ReadDirectDebits","ReadProducts","ReadTransactionsDebits"],
    //    "ExpirationDateTime":"2025-06-11T00:00:00Z",
    //    "TransactionFromDateTime":"1995-07-15T00:00:00Z",
    //    "TransactionToDateTime":"2037-12-31T23:59:59Z",
    //    "ConsentId":"de6786de-9a17-4a79-815b-fab890beee93",
    //    "Status":"AwaitingAuthorisation",
    //    "CreationDateTime":"2022-01-10T16:55:53.136213Z",
    //    "StatusUpdateDateTime":"2022-01-10T16:55:53.136213Z"},
    //  "Risk":{},
    //  "Links":
    //    {"Self":"https://sandbox.hsbc.com/psd2/obie/v3.1/account-access-consents/de6786de-9a17-4a79-815b-fab890beee93"},
    //    "Meta":{}
    @JsonProperty("Permissions")
    String[] permissions;

    @JsonProperty("ConsentId")
    String consentId;
}
