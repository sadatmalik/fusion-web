package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.exceptions.NoSuchIncomeException;
import com.sadatmalik.fusionweb.exceptions.NoSuchMonthlyExpenseException;
import com.sadatmalik.fusionweb.exceptions.NoSuchMonthlyIncomeException;
import com.sadatmalik.fusionweb.exceptions.NoSuchWeeklyExpenseException;
import com.sadatmalik.fusionweb.model.*;
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

import static com.sadatmalik.fusionweb.controllers.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({IncomeExpenseController.class})
@AutoConfigureMockMvc
class IncomeExpenseControllerTest extends ControllerTestBase {

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
        MonthlyExpenseDto missingNameAndAccountId = MonthlyExpenseDto.builder()
                //.name("BROADBAND")
                .amount(31.95)
                .dayOfMonthPaid(18)
                .type(ExpenseType.UTILITIES)
                //.accountId("TEST")
                .build();

        mockMvc
                .perform(post("/income-and-expenses/new-monthly-expense")
                        .flashAttr("monthlyExpenseDto", missingNameAndAccountId)
                        .with(user(principal))
                )

                .andExpect(model().attributeHasFieldErrors("monthlyExpenseDto",
                        "name", "accountId"))
                .andExpect(model().attribute("showNewMonthlyExpenseForm",
                        Matchers.equalTo(true)))

                .andExpect(model().attribute("weeklyExpenseDto",
                        Matchers.any(WeeklyExpenseDto.class)))
                .andExpect(model().attribute("monthlyIncomeDto",
                        Matchers.any(MonthlyIncomeDto.class)))
                .andExpect(model().attribute("weeklyIncomeDto",
                        Matchers.any(WeeklyIncomeDto.class)))

                .andExpect(status().isOk())
                .andExpect(view().name("income-and-expenses"));
    }

    @Test
    void testSaveWeeklyExpenseSuccess() throws Exception {
        WeeklyExpenseDto validExpenseDto = mockWeeklyExpenseDto();
        WeeklyExpense expense = mockWeeklyExpense();

        given(
                incomeExpenseService.saveWeeklyExpense(any(WeeklyExpenseDto.class),
                        any(User.class))).willReturn(expense);

        mockMvc
                .perform(post("/income-and-expenses/new-weekly-expense")
                        .flashAttr("weeklyExpenseDto", validExpenseDto)
                        .with(user(principal))
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/income-and-expenses"));
    }

    @Test
    void testSaveWeeklyExpenseWithValidationErrors() throws Exception {
        WeeklyExpenseDto missingAmountAndTimesPerWeek = WeeklyExpenseDto.builder()
                .accountId("TEST")
                .name("Sainsburys")
                //.amount(80d)
                //.timesPerWeek(1)
                .weeklyInterval(1)
                .type(ExpenseType.GROCERIES)
                .build();

        mockMvc
                .perform(post("/income-and-expenses/new-weekly-expense")
                        .flashAttr("weeklyExpenseDto", missingAmountAndTimesPerWeek)
                        .with(user(principal))
                )

                .andExpect(model().attributeHasFieldErrors("weeklyExpenseDto",
                        "amount", "timesPerWeek"))
                .andExpect(model().attribute("showNewWeeklyExpenseForm",
                        Matchers.equalTo(true)))

                .andExpect(model().attribute("monthlyExpenseDto",
                        Matchers.any(MonthlyExpenseDto.class)))
                .andExpect(model().attribute("monthlyIncomeDto",
                        Matchers.any(MonthlyIncomeDto.class)))
                .andExpect(model().attribute("weeklyIncomeDto",
                        Matchers.any(WeeklyIncomeDto.class)))

                .andExpect(status().isOk())
                .andExpect(view().name("income-and-expenses"));
    }

    @Test
    void testSaveMonthlyIncomeSuccess() throws Exception {
        MonthlyIncomeDto validIncomeDto = mockMonthlyIncomeDto();
        MonthlyIncome income = mockMonthlyIncome();

        given(
                incomeExpenseService.saveMonthlyIncome(any(MonthlyIncomeDto.class),
                        any(User.class))).willReturn(income);

        mockMvc
                .perform(post("/income-and-expenses/new-monthly-income")
                        .flashAttr("monthlyIncomeDto", validIncomeDto)
                        .with(user(principal))
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/income-and-expenses"));
    }

    @Test
    void testSaveMonthlyIncomeWithValidationErrors() throws Exception {
        MonthlyIncomeDto missingSourceAndDayOfMonthReceived = MonthlyIncomeDto.builder()
                .accountId("TEST")
                .amount(1850d)
                //.source("Rent")
                //.dayOfMonthReceived(7)
                .build();

        mockMvc
                .perform(post("/income-and-expenses/new-monthly-income")
                        .flashAttr("monthlyIncomeDto", missingSourceAndDayOfMonthReceived)
                        .with(user(principal))
                )

                .andExpect(model().attributeHasFieldErrors("monthlyIncomeDto",
                        "source", "dayOfMonthReceived"))
                .andExpect(model().attribute("showNewMonthlyIncomeForm",
                        Matchers.equalTo(true)))

                .andExpect(model().attribute("monthlyExpenseDto",
                        Matchers.any(MonthlyExpenseDto.class)))
                .andExpect(model().attribute("weeklyExpenseDto",
                        Matchers.any(WeeklyExpenseDto.class)))
                .andExpect(model().attribute("weeklyIncomeDto",
                        Matchers.any(WeeklyIncomeDto.class)))

                .andExpect(status().isOk())
                .andExpect(view().name("income-and-expenses"));

    }

    @Test
    void testSaveWeeklyIncomeSuccess() throws Exception {
        WeeklyIncomeDto validIncomeDto = mockWeeklyIncomeDto();
        Income income = mockWeeklyIncome();

        given(
                incomeExpenseService.saveWeeklyIncome(any(WeeklyIncomeDto.class),
                        any(User.class))).willReturn(income);

        mockMvc
                .perform(post("/income-and-expenses/new-weekly-income")
                        .flashAttr("weeklyIncomeDto", validIncomeDto)
                        .with(user(principal))
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/income-and-expenses"));
    }

    @Test
    void testSaveWeeklyIncomeWithValidationErrors() throws Exception {
        WeeklyIncomeDto missingAmountAndWeeklyInterval = WeeklyIncomeDto.builder()
                .accountId("TEST")
                //.amount(35d)
                .source("Job")
                //.weeklyInterval(2)
                .build();

        mockMvc
                .perform(post("/income-and-expenses/new-weekly-income")
                        .flashAttr("weeklyIncomeDto", missingAmountAndWeeklyInterval)
                        .with(user(principal))
                )

                .andExpect(model().attributeHasFieldErrors("weeklyIncomeDto",
                        "amount", "weeklyInterval"))
                .andExpect(model().attribute("showNewWeeklyIncomeForm",
                        Matchers.equalTo(true)))

                .andExpect(model().attribute("monthlyExpenseDto",
                        Matchers.any(MonthlyExpenseDto.class)))
                .andExpect(model().attribute("weeklyExpenseDto",
                        Matchers.any(WeeklyExpenseDto.class)))
                .andExpect(model().attribute("monthlyIncomeDto",
                        Matchers.any(MonthlyIncomeDto.class)))

                .andExpect(status().isOk())
                .andExpect(view().name("income-and-expenses"));
    }

    @Test
    void testViewMonthlyExpenseDetails() throws Exception {
        when(incomeExpenseService.getMonthlyExpenseFor(1L))
                .thenReturn(mockMonthlyExpense());

        mockMvc
                .perform(get("/income-and-expenses/1/view")
                        .with(user(principal)))

                .andExpect(model().attribute("editableMonthlyExpenseDto",
                        Matchers.any(MonthlyExpenseDto.class)))
                .andExpect(model().attribute("showMonthlyExpenseEditDeleteForm",
                        Matchers.equalTo(true)))

                .andExpect(model().attribute("monthlyExpenseDto", Matchers.any(MonthlyExpenseDto.class)))
                .andExpect(model().attribute("weeklyExpenseDto", Matchers.any(WeeklyExpenseDto.class)))
                .andExpect(model().attribute("monthlyIncomeDto", Matchers.any(MonthlyIncomeDto.class)))
                .andExpect(model().attribute("weeklyIncomeDto", Matchers.any(WeeklyIncomeDto.class)))

                .andExpect(model().attribute("monthlyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attribute("weeklyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attribute("monthlyIncomeList", Matchers.any(List.class)))
                .andExpect(model().attribute("weeklyIncomeList", Matchers.any(List.class)))

                .andExpect(view().name(("income-and-expenses")));
    }

    @Test
    void testViewWeeklyExpenseDetails() throws Exception {
        when(incomeExpenseService.getWeeklyExpenseFor(1L))
                .thenReturn(mockWeeklyExpense());

        mockMvc
                .perform(get("/income-and-expenses/weekly-expense/1/view")
                        .with(user(principal)))

                .andExpect(model().attribute("editableWeeklyExpenseDto",
                        Matchers.any(WeeklyExpenseDto.class)))
                .andExpect(model().attribute("showWeeklyExpenseEditDeleteForm",
                        Matchers.equalTo(true)))

                .andExpect(model().attribute("monthlyExpenseDto", Matchers.any(MonthlyExpenseDto.class)))
                .andExpect(model().attribute("weeklyExpenseDto", Matchers.any(WeeklyExpenseDto.class)))
                .andExpect(model().attribute("monthlyIncomeDto", Matchers.any(MonthlyIncomeDto.class)))
                .andExpect(model().attribute("weeklyIncomeDto", Matchers.any(WeeklyIncomeDto.class)))

                .andExpect(model().attribute("monthlyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attribute("weeklyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attribute("monthlyIncomeList", Matchers.any(List.class)))
                .andExpect(model().attribute("weeklyIncomeList", Matchers.any(List.class)))

                .andExpect(view().name(("income-and-expenses")));
    }

    @Test
    void testViewMonthlyIncomeDetails() throws Exception {
        when(incomeExpenseService.getMonthlyIncomeFor(1L))
                .thenReturn(mockMonthlyIncome());

        mockMvc
                .perform(get("/income-and-expenses/monthly-income/1/view")
                        .with(user(principal)))

                .andExpect(model().attribute("editableMonthlyIncomeDto",
                        Matchers.any(MonthlyIncomeDto.class)))
                .andExpect(model().attribute("showMonthlyIncomeEditDeleteForm",
                        Matchers.equalTo(true)))

                .andExpect(model().attribute("monthlyExpenseDto", Matchers.any(MonthlyExpenseDto.class)))
                .andExpect(model().attribute("weeklyExpenseDto", Matchers.any(WeeklyExpenseDto.class)))
                .andExpect(model().attribute("monthlyIncomeDto", Matchers.any(MonthlyIncomeDto.class)))
                .andExpect(model().attribute("weeklyIncomeDto", Matchers.any(WeeklyIncomeDto.class)))

                .andExpect(model().attribute("monthlyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attribute("weeklyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attribute("monthlyIncomeList", Matchers.any(List.class)))
                .andExpect(model().attribute("weeklyIncomeList", Matchers.any(List.class)))

                .andExpect(view().name(("income-and-expenses")));
    }

    @Test
    void testViewWeeklyIncomeDetails() throws Exception {
        when(incomeExpenseService.getWeeklyIncomeFor(1L))
                .thenReturn(mockWeeklyIncome());

        mockMvc
                .perform(get("/income-and-expenses/weekly-income/1/view")
                        .with(user(principal)))

                .andExpect(model().attribute("editableWeeklyIncomeDto",
                        Matchers.any(WeeklyIncomeDto.class)))
                .andExpect(model().attribute("showWeeklyIncomeEditDeleteForm",
                        Matchers.equalTo(true)))

                .andExpect(model().attribute("monthlyExpenseDto", Matchers.any(MonthlyExpenseDto.class)))
                .andExpect(model().attribute("weeklyExpenseDto", Matchers.any(WeeklyExpenseDto.class)))
                .andExpect(model().attribute("monthlyIncomeDto", Matchers.any(MonthlyIncomeDto.class)))
                .andExpect(model().attribute("weeklyIncomeDto", Matchers.any(WeeklyIncomeDto.class)))

                .andExpect(model().attribute("monthlyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attribute("weeklyExpenseList", Matchers.any(List.class)))
                .andExpect(model().attribute("monthlyIncomeList", Matchers.any(List.class)))
                .andExpect(model().attribute("weeklyIncomeList", Matchers.any(List.class)))

                .andExpect(view().name(("income-and-expenses")));
    }

    @Test
    void testUpdateMonthlyExpenseSuccess() throws Exception, NoSuchMonthlyExpenseException {
        given(
                incomeExpenseService
                        .updateMonthlyExpense(mockMonthlyExpenseDto()))
                .willReturn(mockMonthlyExpense()
        );

        mockMvc
                .perform(post("/income-and-expenses/monthly-expense/1/edit")
                        .flashAttr("monthlyExpenseDto", mockMonthlyExpenseDto())
                        .with(user(principal))
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/income-and-expenses"));
    }

    @Test
    void testUpdateWeeklyExpenseSuccess() throws NoSuchWeeklyExpenseException, Exception {
        given(
                incomeExpenseService
                        .updateWeeklyExpense(mockWeeklyExpenseDto()))
                .willReturn(mockWeeklyExpense()
                );

        mockMvc
                .perform(post("/income-and-expenses/weekly-expense/1/edit")
                        .flashAttr("weeklyExpenseDto", mockWeeklyExpenseDto())
                        .with(user(principal))
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/income-and-expenses"));
    }

    @Test
    void testUpdateMonthlyIncomeSuccess() throws NoSuchMonthlyIncomeException, Exception {
        given(
                incomeExpenseService
                        .updateMonthlyIncome(mockMonthlyIncomeDto()))
                .willReturn(mockMonthlyIncome()
                );

        mockMvc
                .perform(post("/income-and-expenses/monthly-income/1/edit")
                        .flashAttr("monthlyIncomeDto", mockMonthlyIncomeDto())
                        .with(user(principal))
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/income-and-expenses"));
    }

    @Test
    void testUpdateWeeklyIncomeSuccess() throws NoSuchIncomeException, Exception {
        given(
                incomeExpenseService
                        .updateWeeklyIncome(mockWeeklyIncomeDto()))
                .willReturn(mockWeeklyIncome()
                );

        mockMvc
                .perform(post("/income-and-expenses/weekly-income/1/edit")
                        .flashAttr("weeklyIncomeDto", mockWeeklyIncomeDto())
                        .with(user(principal))
                )

                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/income-and-expenses"));
    }

    @Test
    void testDeleteMonthlyExpense() throws Exception {
        mockMvc
                .perform(get("/income-and-expenses/1/delete")
                        .with(user(principal))
                )
                .andExpect(view().name("redirect:/income-and-expenses"));

        verify(incomeExpenseService, times(1))
                .deleteMonthlyExpenseFor(1L);
    }

    @Test
    void testDeleteWeeklyExpense() throws Exception {
        mockMvc
                .perform(get("/income-and-expenses/weekly-expense/1/delete")
                        .with(user(principal))
                )
                .andExpect(view().name("redirect:/income-and-expenses"));

        verify(incomeExpenseService, times(1))
                .deleteWeeklyExpenseFor(1L);
    }

    @Test
    void testDeleteMonthlyIncome() throws Exception {
        mockMvc
                .perform(get("/income-and-expenses/monthly-income/1/delete")
                        .with(user(principal))
                )
                .andExpect(view().name("redirect:/income-and-expenses"));

        verify(incomeExpenseService, times(1))
                .deleteMonthlyIncomeFor(1L);
    }

    @Test
    void testDeleteWeeklyIncome() throws Exception {
        mockMvc
                .perform(get("/income-and-expenses/weekly-income/1/delete")
                        .with(user(principal))
                )
                .andExpect(view().name("redirect:/income-and-expenses"));

        verify(incomeExpenseService, times(1))
                .deleteWeeklyIncomeFor(1L);
    }
}