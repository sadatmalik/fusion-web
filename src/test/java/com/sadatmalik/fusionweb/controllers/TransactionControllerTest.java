package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.Account;
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

import java.util.ArrayList;

import static com.sadatmalik.fusionweb.controllers.TestUtils.mockAccount;
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
    void transactionsByAccount() throws Exception {

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
    void allTransactions() {
    }
}