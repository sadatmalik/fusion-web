package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.Transaction;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.services.AccountService;
import com.sadatmalik.fusionweb.services.AccountServicesRegistry;
import com.sadatmalik.fusionweb.services.TransactionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sadatmalik.fusionweb.controllers.TestUtils.mockAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest({TransactionController.class})
@AutoConfigureMockMvc
class TransactionControllerTest extends ControllerTestBase {

    @Autowired
    TransactionController controller;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountServicesRegistry accountServicesRegistry;

    @MockBean
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        createPrincipal();
    }

    @Test
    public void testWebOnlyContextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testTransactionsByAccount() throws Exception {

        given(accountService.getAccountById("1"))
                .willReturn(mockAccount());

        given(accountServicesRegistry.getTransactionServiceFor(any(Account.class)))
                .willReturn(transactionService);

        given(transactionService.getTransactions(any(Account.class)))
                .willReturn(new ArrayList<>());

        mockMvc
                .perform(get("/transactions/1")
                        .with(user(principal)))

                .andExpect(model().attribute("account", Matchers.any(Account.class)))
                .andExpect(model().attribute("totalBalance", Matchers.equalTo("£1,234.56")))
                .andExpect(model().attributeExists("transactions"))

                .andExpect(view().name("account-transactions"));

    }

    @Test
    void testAllTransactions() throws Exception {
        given(accountService.getAccountsFor(any(User.class)))
                .willReturn(List.of(
                        mockAccount(), mockAccount(), mockAccount()
                ));

        given(accountServicesRegistry.getTransactionServiceFor(any(Account.class)))
                .willReturn(transactionService);

        given(transactionService.getTransactions(any(Account.class)))
                .willReturn(List.of(
                        Transaction.builder()
                                .date(new Date())
                                .type("VIS")
                                .description("TEST EXPENSE")
                                .category("TRANSPORT")
                                .amount(new BigDecimal(5.00))
                                .build(),

                        Transaction.builder()
                                .date(new Date())
                                .type("VIS")
                                .description("TEST EXPENSE 2")
                                .category("FOOD & DRINK")
                                .amount(new BigDecimal(3.07))
                                .build()
                ));

        mockMvc
                .perform(get("/transactions")
                        .with(user(principal)))

                .andExpect(model().attribute("totalBalance", "£3,703.68"))
                .andExpect(model().attribute("transactions", Matchers.iterableWithSize(6)))

                .andExpect(view().name("transactions"));

    }
}