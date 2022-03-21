package com.sadatmalik.fusionweb.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({AddAccountController.class})
@AutoConfigureMockMvc
class AddAccountControllerTest extends ControllerTestBase {

    @Autowired
    AddAccountController controller;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        createPrincipal();
    }

    @Test
    public void testWebOnlyContextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testAddAccountView() throws Exception {
        mockMvc.perform(get("/add-account")
                        .with(user(principal)))

                .andDo(print())

                .andExpect(status().isOk())
                .andExpect(view().name(("addaccount")));
    }

    @Test
    void testAddHsbcAccountView() throws Exception {
        mockMvc.perform(get("/add-account/hsbc")
                        .with(user(principal)))

                .andDo(print())

                .andExpect(status().isOk())
                .andExpect(view().name(("addaccount-hsbc")));
    }
}