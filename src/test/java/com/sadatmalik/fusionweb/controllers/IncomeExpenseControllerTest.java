package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyIncomeDto;
import com.sadatmalik.fusionweb.services.IncomeExpenseService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.sadatmalik.fusionweb.controllers.TestUtils.mockMonthlyExpense;
import static com.sadatmalik.fusionweb.controllers.TestUtils.mockMonthlyExpenseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({IncomeExpenseController.class})
@AutoConfigureMockMvc
class IncomeExpenseControllerTest extends TestBase {

    @Autowired
    IncomeExpenseController controller;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IncomeExpenseService incomeExpenseService;

    @BeforeEach
    void setUp() {
        createPrincipal();
    }

    @Test
    public void testWebOnlyContextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testIncomeAndExpensesModelAndView() throws Exception {
        mockMvc.perform(get("/income-and-expenses")
                .with(user(principal)))

                .andExpect(status().isOk())

                .andExpect(model().attributeExists("monthlyExpenseDto"))
                .andExpect(model().attribute("monthlyExpenseDto", Matchers.any(MonthlyExpenseDto.class)))
                .andExpect(model().attributeExists("weeklyExpenseDto"))
                .andExpect(model().attribute("weeklyExpenseDto", Matchers.any(WeeklyExpenseDto.class)))
                .andExpect(model().attributeExists("monthlyIncomeDto"))
                .andExpect(model().attribute("monthlyIncomeDto", Matchers.any(MonthlyIncomeDto.class)))
                .andExpect(model().attributeExists("weeklyIncomeDto"))
                .andExpect(model().attribute("weeklyIncomeDto", Matchers.any(WeeklyIncomeDto.class)))

                .andExpect(model().attributeExists("monthlyExpenseList"))
                .andExpect(model().attribute("monthlyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attributeExists("weeklyExpenseList"))
                .andExpect(model().attribute("weeklyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attributeExists("monthlyIncomeList"))
                .andExpect(model().attribute("monthlyIncomeList", Matchers.any(List.class)))
                .andExpect(model().attributeExists("weeklyIncomeList"))
                .andExpect(model().attribute("weeklyIncomeList", Matchers.any(List.class)))

                .andExpect(view().name(("income-and-expenses")));
    }

    @Test
    void testSaveMonthlyExpenseSuccess() throws Exception {
        given(
                incomeExpenseService.saveMonthlyExpense(any(MonthlyExpenseDto.class),
                any(User.class))).willReturn(mockMonthlyExpense()
        );

        mockMvc
                .perform(post("/income-and-expenses/new-monthly-expense")
                        .flashAttr("monthlyExpenseDto", mockMonthlyExpenseDto())
                        .with(user(principal))
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/income-and-expenses"));
    }

    @Test
    void testSaveMonthlyExpenseValidationErrors() throws Exception {

    }

    @Test
    void saveWeeklyExpense() {
    }

    @Test
    void saveMonthlyIncome() {
    }

    @Test
    void saveWeeklyIncome() {
    }

    @Test
    void viewMonthlyExpenseDetails() {
    }

    @Test
    void viewWeeklyExpenseDetails() {
    }

    @Test
    void viewMonthlyIncomeDetails() {
    }

    @Test
    void viewWeeklyIncomeDetails() {
    }

    @Test
    void updateMonthlyExpense() {
    }

    @Test
    void updateWeeklyExpense() {
    }

    @Test
    void updateMonthlyIncome() {
    }

    @Test
    void updateWeeklyIncome() {
    }

    @Test
    void deleteMonthlyExpense() {
    }

    @Test
    void deleteWeeklyExpense() {
    }

    @Test
    void deleteMonthlyIncome() {
    }

    @Test
    void deleteWeeklyIncome() {
    }
}