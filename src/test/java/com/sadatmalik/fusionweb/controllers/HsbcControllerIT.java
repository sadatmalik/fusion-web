package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.oauth.hsbc.HsbcAuthenticationEndpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
class HsbcControllerIT extends ControllerTestBase {

    @Autowired
    HsbcController controller;

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
    void testHsbcAuthorizationUrl() throws Exception {
        mockMvc.perform(get("/hsbc")
                        .with(user(principal)))

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(containsString("redirect:" +
                        HsbcAuthenticationEndpoints.AUTHORIZE_URL)));
    }

    @Test
    void testHsbcCallbackWithoutAuthCode() throws Exception {
        mockMvc.perform(get("/")
                        .with(user(principal)))

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(
                        containsString("redirect:/quickstats")));
    }

    @Test
    void testHsbcCallbackWithAuthCode() throws Exception {
        mockMvc.perform(get("/?code=1234")
                        .with(user(principal)))

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(
                        containsString("redirect:/dashboard")));
    }
}