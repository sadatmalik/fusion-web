package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.FusionWebPrototypeApplication;
import com.sadatmalik.fusionweb.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.sadatmalik.fusionweb.controllers.TestUtils.mockAccount;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FusionWebPrototypeApplication.class)
@ActiveProfiles("test")
class AccountServicesRegistryTest {

    @Autowired
    private AccountServicesRegistry accountServicesRegistry;

    private TransactionService transactionService;

    private Account account;

    @BeforeEach
    public void setup() {
        transactionService = new DummyTransactionService();
        account = mockAccount();

        accountServicesRegistry.registerTransactionService(
                account, transactionService
        );
    }

    @Test
    public void testServiceContextLoads() {
        assertThat(accountServicesRegistry).isNotNull();
    }

    @Test
    void getTransactionServiceFor() {
        assertThat(
                accountServicesRegistry
                        .getTransactionServiceFor(account))
                .isEqualTo(transactionService);
    }
}