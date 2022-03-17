package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.services.AccountService;
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

@WebMvcTest({DashboardController.class})
@AutoConfigureMockMvc
class DashboardControllerTest extends TestBase {

    @Autowired
    private DashboardController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        createPrincipal();
    }

    @Test
    public void testWebOnlyContextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testGetDashboardModelAndView() throws Exception {
        mockMvc.perform(get("/dashboard")
                        .with(user(principal)))

                .andDo(print())

                .andExpect(status().isOk())

                .andExpect(model().attributeExists("accountList"))
                .andExpect(model().attribute("totalBalance", "£0.00"))
                .andExpect(model().attribute("date",
                        LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM, dd yyyy"))))

                .andExpect(view().name(("dashboard")));

    }
}