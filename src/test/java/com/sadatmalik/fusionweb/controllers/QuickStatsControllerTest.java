package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.websecurity.Authority;
import com.sadatmalik.fusionweb.model.websecurity.RoleEnum;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.AccountService;
import com.sadatmalik.fusionweb.services.DebtService;
import com.sadatmalik.fusionweb.services.IncomeExpenseService;
import com.sadatmalik.fusionweb.services.websecurity.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({QuickStatsController.class})
@AutoConfigureMockMvc
class QuickStatsControllerTest {

    private UserPrincipal principal;

    @Autowired
    QuickStatsController controller;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    AccountService accountService;

    @MockBean
    IncomeExpenseService incomeExpenseService;

    @MockBean
    DebtService debtService;

    @Test
    public void testWebOnlyContextLoads() {
        assertThat(controller).isNotNull();
    }

    @BeforeEach
    void setUp() {
        Authority userAuth = Authority.builder()
                .authority(RoleEnum.ROLE_USER).build();
        principal = new UserPrincipal("USER",
                "user", Collections.singletonList(userAuth));

        User user = User.builder()
                .firstName("Sadat")
                .lastName("Malik")
                .email("sm@creativefusion.net")
                .build();

        principal.setUser(user);
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