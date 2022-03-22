package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.FusionWebPrototypeApplication;
import com.sadatmalik.fusionweb.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.sadatmalik.fusionweb.controllers.TestUtils.mockAccount;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FusionWebPrototypeApplication.class)
@ActiveProfiles("test")
class DummyBarclaysSavingsTransactionServiceTest {

    @Autowired
    @Qualifier("dummyBarclaysSavingsTransactionService")
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testServiceContextLoads() {
        assertThat(transactionService).isNotNull();
    }

    @Test
    void getTransactions() {
        assertThat(
                transactionService
                        .getTransactions(mockAccount()))
                .hasOnlyElementsOfType(Transaction.class);

        assertThat(
                transactionService
                        .getTransactions(mockAccount()))
                .hasSize(14);
    }
}