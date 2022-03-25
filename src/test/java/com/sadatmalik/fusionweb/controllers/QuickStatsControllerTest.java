package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.services.AccountService;
import com.sadatmalik.fusionweb.services.DebtService;
import com.sadatmalik.fusionweb.services.IncomeExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({QuickStatsController.class})
@AutoConfigureMockMvc
class QuickStatsControllerTest extends ControllerTestBase {

    @Autowired
    QuickStatsController controller;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @MockBean
    IncomeExpenseService incomeExpenseService;

    @MockBean
    DebtService debtService;

    @BeforeEach
    void setUp() {
        createPrincipal();
    }

    @Test
    public void testWebOnlyContextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testQuickStatsReturnsQuickstatsModelAndView() throws Exception {
        mockMvc.perform(get("/quickstats")
                        .with(user(principal)))

                .andDo(print())

                .andExpect(status().isOk())
                .andExpect(view().name(("quickstats")))
                .andExpect(model().attribute("totalBalance", "£0.00"))
                .andExpect(model().attributeExists("chartData"))
                .andExpect(model().attributeExists("paymentChartData"))
                .andExpect(model().attributeExists("debtChartData"))
                .andExpect(model().attribute("date",
                        LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM, dd yyyy"))));

    }

    @Test
    void testInvalidUrlReturnsNotFound() throws Exception {
        mockMvc.perform(get("/quickstats/nosuhresource")
                        .with(user(principal)))

                .andExpect(status().isNotFound());
    }

}