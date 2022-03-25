package com.sadatmalik.fusionweb.services.hsbc;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.AccountType;
import com.sadatmalik.fusionweb.model.Bank;
import com.sadatmalik.fusionweb.oauth.hsbc.HsbcUserAccessToken;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.HsbcAccount;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.HsbcAccountData;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.HsbcAccountInfo;
import com.sadatmalik.fusionweb.services.hsbc.model.accounts.HsbcAccountList;
import com.sadatmalik.fusionweb.services.hsbc.model.balances.HsbcAmount;
import com.sadatmalik.fusionweb.services.hsbc.model.balances.HsbcBalance;
import com.sadatmalik.fusionweb.services.hsbc.model.balances.HsbcBalanceData;
import com.sadatmalik.fusionweb.services.hsbc.model.balances.HsbcBalanceObject;
import com.sadatmalik.fusionweb.services.hsbc.model.transacations.HsbcTransaction;
import com.sadatmalik.fusionweb.services.hsbc.model.transacations.HsbcTransactionData;
import com.sadatmalik.fusionweb.services.hsbc.model.transacations.HsbcTransactionObject;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author sm@creativefusion.net
 */
public class HsbcApiServiceTestUtils {

    static HsbcUserAccessToken mockValidUserAccessToken() {
        HsbcUserAccessToken userAccessToken = new HsbcUserAccessToken();

        userAccessToken.setAccessToken("d13a4f6c-ce35-4b3b-aadd-0ee14563a04e");
        userAccessToken.setSecondsToExpire(3599);
        userAccessToken.setTokenType("Bearer");
        userAccessToken.setRefreshToken("701861fa-3182-440c-9af0-d9ff285b51f6");

        return userAccessToken;
    }

    static ResponseEntity<HsbcAccountList> mockHsbcAccountListResponseEntity() {
        HsbcAccountList accountList = mockHsbcAccountList();

        assert accountList != null;

        return ResponseEntity.of(Optional.of(accountList));
    }

    private static HsbcAccountList mockHsbcAccountList() {
        HsbcAccountList accountList = new HsbcAccountList();
        accountList.setData(mockHsbcAccountData());

        return accountList;
    }

    private static HsbcAccountData mockHsbcAccountData() {
        HsbcAccountData data = new HsbcAccountData();
        data.setAccounts(List.of(mockHsbcAccount()));

        return data;
    }

    static HsbcAccount mockHsbcAccount() {
        return HsbcAccount.builder()
                .bank("HSBC")
                .accountId("AB12345678")
                .currency("GBP")
                .type("Current")
                .subType("Advance")
                .description("Hsbc Advance account")
                .accountInfo(mockHsbcAccountInfo())
                .build();
    }

    private static List<HsbcAccountInfo> mockHsbcAccountInfo() {
        HsbcAccountInfo info = new HsbcAccountInfo();
        info.setScheme("IBAN");
        info.setIdentifier("IBAN1234567890");
        info.setName("Mr John Smith");

        return List.of(info);
    }

    static ResponseEntity<HsbcBalanceObject> mockHsbcBalanceObjectResponseEntity() {
        HsbcBalanceObject balance = mockHsbcBalanceObject();

        assert balance != null;

        return ResponseEntity.of(Optional.of(balance));
    }

    private static HsbcBalanceObject mockHsbcBalanceObject() {
        HsbcBalanceObject balanceObject = new HsbcBalanceObject();
        balanceObject.setData(mockHsbcBalanceData());

        return balanceObject;
    }

    private static HsbcBalanceData mockHsbcBalanceData() {
        HsbcBalanceData data = new HsbcBalanceData();
        data.setBalanceList(List.of(mockHsbcBalance()));

        return data;
    }

    private static HsbcBalance mockHsbcBalance() {
        HsbcAmount amount = getAmount(12345d);

        return HsbcBalance.builder()
                .accountId("AB12345678")
                .creditDebit("Credit")
                .amount(amount)
                .build();
    }

    private static HsbcAmount getAmount(double value) {
        HsbcAmount amount = new HsbcAmount();
        amount.setAmount(value);
        amount.setCurrency("GBP");
        return amount;
    }

    static Account mockAccount() {
        return Account.builder()
                .bank(Bank.builder()
                        .name(mockHsbcAccount().getBank())
                        .build())
                .accountId(
                        mockHsbcAccount().getAccountId())
                .currency(
                        mockHsbcAccount().getCurrency())
                .type(AccountType.CURRENT)
                .description(
                        mockHsbcAccount().getDescription())
                .build();
    }

    static ResponseEntity<HsbcTransactionObject> mockHsbcTransactionObjectResponseEntity() {
        HsbcTransactionObject transaction = mockHsbcTransactionObject();

        assert transaction != null;

        return ResponseEntity.of(Optional.of(transaction));

    }

    private static HsbcTransactionObject mockHsbcTransactionObject() {
        HsbcTransactionObject transactionObject = new HsbcTransactionObject();
        transactionObject.setData(mockHsbcTransactionData());

        return transactionObject;
    }

    private static HsbcTransactionData mockHsbcTransactionData() {
        HsbcTransactionData data = new HsbcTransactionData();
        data.setTransactionList(List.of(mockHsbcTransaction()));

        return data;
    }

    private static HsbcTransaction mockHsbcTransaction() {
        HsbcAmount amount = getAmount(12.345d);

        HsbcTransaction transaction = new HsbcTransaction();
        transaction.setAccountId(mockAccount().getAccountId());
        transaction.setTransactionId("TXN_ID_TEST_1234");
        transaction.setCreditDebitIndicator("Credit");
        transaction.setStatus("Booked");
        transaction.setBookingDateTime("2022-03-25T18:12:03Z");
        transaction.setHsbcAmount(amount);

        return transaction;
    }


}
