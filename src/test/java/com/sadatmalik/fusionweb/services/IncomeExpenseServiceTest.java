package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.FusionWebPrototypeApplication;
import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.repositories.IncomeRepository;
import com.sadatmalik.fusionweb.repositories.MonthlyExpenseRepository;
import com.sadatmalik.fusionweb.repositories.MonthlyIncomeRepository;
import com.sadatmalik.fusionweb.repositories.WeeklyExpenseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.sadatmalik.fusionweb.controllers.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = FusionWebPrototypeApplication.class)
@ActiveProfiles("test")
class IncomeExpenseServiceTest {

    @Autowired
    private IncomeExpenseService incomeExpenseService;

    @MockBean
    private MonthlyExpenseRepository monthlyExpenseRepository;

    @MockBean
    private WeeklyExpenseRepository weeklyExpenseRepository;

    @MockBean
    private MonthlyIncomeRepository monthlyIncomeRepository;

    @MockBean
    private IncomeRepository incomeRepository;

    @MockBean
    private AccountService accountService;

    private static User user;

    @BeforeAll
    static void setUp() {
        user = mockUser();
    }

    @Test
    void testServiceContextLoads() {
        assertThat(incomeExpenseService).isNotNull();
    }

    @Test
    void testGetMonthlyExpenseForFound() {
        given(
                monthlyExpenseRepository
                        .findById(1L))
                .willReturn(Optional.of(mockMonthlyExpense()));

        assertThat(incomeExpenseService.getMonthlyExpenseFor(1L))
                .isEqualTo(mockMonthlyExpense());

        verify(monthlyExpenseRepository, times(1))
                .findById(any(Long.class));
    }

    @Test
    void testGetMonthlyExpenseForNotFound() {
        given(
                monthlyExpenseRepository
                        .findById(1L))
                .willReturn(Optional.empty());

        assertThat(incomeExpenseService.getMonthlyExpenseFor(1L))
                .isNull();

        verify(monthlyExpenseRepository, times(1))
                .findById(any(Long.class));
    }

    @Test
    void testGetMonthlyExpensesForFound() {
        given(
                monthlyExpenseRepository
                        .findByUser(user))
                .willReturn(List.of(mockMonthlyExpense()));

        assertThat(incomeExpenseService.getMonthlyExpensesFor(user))
                .containsExactly(mockMonthlyExpense());

        verify(monthlyExpenseRepository, times(1))
                .findByUser(any(User.class));
    }

    @Test
    void testGetMonthlyExpensesForNotFound() {
        given(
                monthlyExpenseRepository
                        .findByUser(user))
                .willReturn(List.of());

        assertThat(incomeExpenseService.getMonthlyExpensesFor(user))
                .containsExactly();

        verify(monthlyExpenseRepository, times(1))
                .findByUser(any(User.class));
    }

    @Test
    void testGetWeeklyExpenseForFound() {
        given(
                weeklyExpenseRepository
                        .findById(1L))
                .willReturn(Optional.of(mockWeeklyExpense()));

        assertThat(incomeExpenseService.getWeeklyExpenseFor(1L))
                .isEqualTo(mockWeeklyExpense());

        verify(weeklyExpenseRepository, times(1))
                .findById(any(Long.class));
    }

    @Test
    void testGetWeeklyExpenseForNotFound() {
        given(
                weeklyExpenseRepository
                        .findById(1L))
                .willReturn(Optional.empty());

        assertThat(incomeExpenseService.getWeeklyExpenseFor(1L))
                .isNull();

        verify(weeklyExpenseRepository, times(1))
                .findById(any(Long.class));
    }

    @Test
    void testGetWeeklyExpensesForFound() {
        given(
                weeklyExpenseRepository
                        .findByUser(user))
                .willReturn(List.of(mockWeeklyExpense()));

        assertThat(incomeExpenseService.getWeeklyExpensesFor(user))
                .containsExactly(mockWeeklyExpense());

        verify(weeklyExpenseRepository, times(1))
                .findByUser(any(User.class));
    }

    @Test
    void testGetWeeklyExpensesForNotFound() {
        given(
                weeklyExpenseRepository
                        .findByUser(user))
                .willReturn(List.of());

        assertThat(incomeExpenseService.getWeeklyExpensesFor(user))
                .containsExactly();

        verify(weeklyExpenseRepository, times(1))
                .findByUser(any(User.class));
    }

    @Test
    void testGetMonthlyIncomeForIdFound() {
        given(
                monthlyIncomeRepository
                        .findById(1L))
                .willReturn(Optional.of(mockMonthlyIncome()));

        assertThat(incomeExpenseService.getMonthlyIncomeFor(1L))
                .isEqualTo(mockMonthlyIncome());

        verify(monthlyIncomeRepository, times(1))
                .findById(any(Long.class));
    }

    @Test
    void testGetMonthlyIncomeForIdNotFound() {
        given(
                monthlyIncomeRepository
                        .findById(1L))
                .willReturn(Optional.empty());

        assertThat(incomeExpenseService.getMonthlyIncomeFor(1L))
                .isNull();

        verify(monthlyIncomeRepository, times(1))
                .findById(any(Long.class));
    }

    @Test
    void testGetMonthlyIncomeForUserFound() {
        given(
                monthlyIncomeRepository
                        .findByUser(user))
                .willReturn(List.of(mockMonthlyIncome()));

        assertThat(incomeExpenseService.getMonthlyIncomeFor(user))
                .containsExactly(mockMonthlyIncome());

        verify(monthlyIncomeRepository, times(1))
                .findByUser(any(User.class));
    }

    @Test
    void testGetMonthlyIncomeForUserNotFound() {
        given(
                monthlyIncomeRepository
                        .findByUser(user))
                .willReturn(List.of());

        assertThat(incomeExpenseService.getMonthlyIncomeFor(user))
                .containsExactly();

        verify(monthlyIncomeRepository, times(1))
                .findByUser(any(User.class));
    }

    @Test
    void testGetWeeklyIncomeForIdFound() {
        given(
                incomeRepository
                        .findById(1L))
                .willReturn(Optional.of(mockWeeklyIncome()));

        assertThat(incomeExpenseService.getWeeklyIncomeFor(1L))
                .isEqualTo(mockWeeklyIncome());

        verify(incomeRepository, times(1))
                .findById(any(Long.class));
    }

    @Test
    void testGetWeeklyIncomeForIdNotFound() {
        given(
                incomeRepository
                        .findById(1L))
                .willReturn(Optional.empty());

        assertThat(incomeExpenseService.getWeeklyIncomeFor(1L))
                .isNull();

        verify(incomeRepository, times(1))
                .findById(any(Long.class));
    }

    @Test
    void testGetWeeklyIncomeForUserFound() {
        given(
                incomeRepository
                        .findByUser(user))
                .willReturn(List.of(mockWeeklyIncome()));

        assertThat(incomeExpenseService.getWeeklyIncomeFor(user))
                .containsExactly(mockWeeklyIncome());

        verify(incomeRepository, times(1))
                .findByUser(any(User.class));
    }

    @Test
    void testSaveMonthlyExpense() {

        MonthlyExpense expense = mockMonthlyExpense();

        given(
                accountService
                        .getAccountById(mockMonthlyExpenseDto().getAccountId()))
                .willReturn(mockAccount());

        given(
                monthlyExpenseRepository
                        .save(any(MonthlyExpense.class)))
                .willReturn(expense);

        assertThat(
                incomeExpenseService
                        .saveMonthlyExpense(mockMonthlyExpenseDto(), user)
        ).isEqualTo(expense);

        verify(accountService, times(1))
                .getAccountById(any(String.class));

        verify(monthlyExpenseRepository, times(1))
                .save(any(MonthlyExpense.class));
    }

    @Test
    void testSaveWeeklyExpense() {
        WeeklyExpense expense = mockWeeklyExpense();

        given(
                accountService
                        .getAccountById(mockWeeklyExpenseDto()
                                .getAccountId()))
                .willReturn(mockAccount());

        given(
                weeklyExpenseRepository
                        .save(any(WeeklyExpense.class)))
                .willReturn(expense);

        assertThat(
                incomeExpenseService
                        .saveWeeklyExpense(mockWeeklyExpenseDto(), user)
        ).isEqualTo(expense);

        verify(accountService, times(1))
                .getAccountById(any(String.class));

        verify(weeklyExpenseRepository, times(1))
                .save(any(WeeklyExpense.class));
    }

    @Test
    void testSaveMonthlyIncome() {
        MonthlyIncome income = mockMonthlyIncome();

        given(
                accountService
                        .getAccountById(mockMonthlyIncomeDto()
                                .getAccountId()))
                .willReturn(mockAccount());

        given(
                monthlyIncomeRepository
                        .save(any(MonthlyIncome.class)))
                .willReturn(income);

        assertThat(
                incomeExpenseService
                        .saveMonthlyIncome(mockMonthlyIncomeDto(), user)
        ).isEqualTo(income);

        verify(accountService, times(1))
                .getAccountById(any(String.class));

        verify(monthlyIncomeRepository, times(1))
                .save(any(MonthlyIncome.class));
    }

    @Test
    void saveWeeklyIncome() {
        Income income = mockWeeklyIncome();

        given(
                accountService
                        .getAccountById(mockWeeklyIncomeDto()
                                .getAccountId()))
                .willReturn(mockAccount());

        given(
                incomeRepository
                        .save(any(Income.class)))
                .willReturn(income);

        assertThat(
                incomeExpenseService
                        .saveWeeklyIncome(mockWeeklyIncomeDto(), user)
        ).isEqualTo(income);

        verify(accountService, times(1))
                .getAccountById(any(String.class));

        verify(incomeRepository, times(1))
                .save(any(Income.class));
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
    void deleteMonthlyExpenseFor() {
    }

    @Test
    void deleteWeeklyExpenseFor() {
    }

    @Test
    void deleteMonthlyIncomeFor() {
    }

    @Test
    void deleteWeeklyIncomeFor() {
    }
}