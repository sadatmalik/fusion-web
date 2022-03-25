package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.FusionWebPrototypeApplication;
import com.sadatmalik.fusionweb.exceptions.NoSuchIncomeException;
import com.sadatmalik.fusionweb.exceptions.NoSuchMonthlyExpenseException;
import com.sadatmalik.fusionweb.exceptions.NoSuchMonthlyIncomeException;
import com.sadatmalik.fusionweb.exceptions.NoSuchWeeklyExpenseException;
import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyIncomeDto;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void testUpdateMonthlyExpenseSuccess() throws NoSuchMonthlyExpenseException {
        MonthlyExpenseDto expenseDto = mockMonthlyExpenseDto();
        expenseDto.setId(1L);

        MonthlyExpense expense = mockMonthlyExpense();

        assert expenseDto.getId() != null;

        given(
                monthlyExpenseRepository
                        .findById(expenseDto.getId())
        ).willReturn(Optional.of(expense));

        given(
                accountService
                        .getAccountById(expenseDto.getAccountId())
        ).willReturn(mockAccount());

        given(
                monthlyExpenseRepository
                        .save(expense)
        ).willReturn(expense);

        assertThat(incomeExpenseService
                .updateMonthlyExpense(expenseDto))
                .isEqualTo(expense);
    }

    @Test
    void testUpdateMonthlyExpenseNotFound() {
        MonthlyExpenseDto expenseDto = mockMonthlyExpenseDto();
        expenseDto.setId(1L);

        assert expenseDto.getId() != null;

        given(
                monthlyExpenseRepository
                        .findById(expenseDto.getId())
        ).willReturn(Optional.empty());

        NoSuchMonthlyExpenseException exception = assertThrows(NoSuchMonthlyExpenseException.class, () -> {
            incomeExpenseService
                    .updateMonthlyExpense(expenseDto);
        });

        String expectedMessage = "Trying to update non-existing monthly expense - " +
                "update attempted for monthly expense ID 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateWeeklyExpenseSuccess() throws NoSuchWeeklyExpenseException {
        WeeklyExpenseDto expenseDto = mockWeeklyExpenseDto();
        expenseDto.setId(1L);

        WeeklyExpense expense = mockWeeklyExpense();

        assert expenseDto.getId() != null;

        given(
                weeklyExpenseRepository
                        .findById(expenseDto.getId())
        ).willReturn(Optional.of(expense));

        given(
                accountService
                        .getAccountById(expenseDto.getAccountId())
        ).willReturn(mockAccount());

        given(
                weeklyExpenseRepository
                        .save(expense)
        ).willReturn(expense);

        assertThat(incomeExpenseService
                .updateWeeklyExpense(expenseDto))
                .isEqualTo(expense);
    }

    @Test
    void testUpdateWeeklyExpenseNotFound() {
        WeeklyExpenseDto expenseDto = mockWeeklyExpenseDto();
        expenseDto.setId(1L);

        assert expenseDto.getId() != null;

        given(
                weeklyExpenseRepository
                        .findById(expenseDto.getId())
        ).willReturn(Optional.empty());

        NoSuchWeeklyExpenseException exception = assertThrows(NoSuchWeeklyExpenseException.class, () -> {
            incomeExpenseService
                    .updateWeeklyExpense(expenseDto);
        });

        String expectedMessage = "Trying to update non-existing weekly expense - " +
                "update attempted for weekly expense ID 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateMonthlyIncomeSuccess() throws NoSuchMonthlyIncomeException {
        MonthlyIncomeDto incomeDto = mockMonthlyIncomeDto();
        incomeDto.setId(1L);

        MonthlyIncome income = mockMonthlyIncome();

        assert incomeDto.getId() != null;

        given(
                monthlyIncomeRepository
                        .findById(incomeDto.getId())
        ).willReturn(Optional.of(income));

        given(
                accountService
                        .getAccountById(incomeDto.getAccountId())
        ).willReturn(mockAccount());

        given(
                monthlyIncomeRepository
                        .save(income)
        ).willReturn(income);

        assertThat(incomeExpenseService
                .updateMonthlyIncome(incomeDto))
                .isEqualTo(income);
    }

    @Test
    void testUpdateMonthlyIncomeNotFound() {
        MonthlyIncomeDto incomeDto = mockMonthlyIncomeDto();
        incomeDto.setId(1L);

        assert incomeDto.getId() != null;

        given(
                monthlyIncomeRepository
                        .findById(incomeDto.getId())
        ).willReturn(Optional.empty());

        NoSuchMonthlyIncomeException exception = assertThrows(NoSuchMonthlyIncomeException.class, () -> {
            incomeExpenseService
                    .updateMonthlyIncome(incomeDto);
        });

        String expectedMessage = "Trying to update non-existing monthly income - " +
                "update attempted for monthly income ID 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateWeeklyIncome() throws NoSuchIncomeException {
        WeeklyIncomeDto incomeDto = mockWeeklyIncomeDto();
        incomeDto.setId(1L);

        Income income = mockWeeklyIncome();

        assert incomeDto.getId() != null;

        given(
                incomeRepository
                        .findById(incomeDto.getId())
        ).willReturn(Optional.of(income));

        given(
                accountService
                        .getAccountById(incomeDto.getAccountId())
        ).willReturn(mockAccount());

        given(
                incomeRepository.save(income)
        ).willReturn(income);

        assertThat(incomeExpenseService
                .updateWeeklyIncome(incomeDto))
                .isEqualTo(income);
    }

    @Test
    void testUpdateWeeklyIncomeNotFound() {
        WeeklyIncomeDto incomeDto = mockWeeklyIncomeDto();
        incomeDto.setId(1L);

        assert incomeDto.getId() != null;

        given(
                monthlyIncomeRepository
                        .findById(incomeDto.getId())
        ).willReturn(Optional.empty());

        NoSuchIncomeException exception = assertThrows(NoSuchIncomeException.class, () -> {
            incomeExpenseService
                    .updateWeeklyIncome(incomeDto);
        });

        String expectedMessage = "Trying to update non-existing weekly income - " +
                "update attempted for weekly income ID 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeleteMonthlyExpenseFor() {
        MonthlyExpense expense = mockMonthlyExpense();
        expense.setId(1L);

        assert expense.getId() != null;

        given(
                monthlyExpenseRepository
                        .findById(1L)
        ).willReturn(Optional.of(expense));

        incomeExpenseService.deleteMonthlyExpenseFor(1L);

        verify(monthlyExpenseRepository, times(1))
                .findById(any(Long.class));

        verify(monthlyExpenseRepository, times(1))
                .deleteById(expense.getId());
    }

    @Test
    void testDeleteWeeklyExpenseFor() {
        WeeklyExpense expense = mockWeeklyExpense();
        expense.setId(1L);

        assert expense.getId() != null;

        given(
                weeklyExpenseRepository
                        .findById(1L)
        ).willReturn(Optional.of(expense));

        incomeExpenseService.deleteWeeklyExpenseFor(1L);

        verify(weeklyExpenseRepository, times(1))
                .findById(any(Long.class));

        verify(weeklyExpenseRepository, times(1))
                .deleteById(expense.getId());
    }

    @Test
    void testDeleteMonthlyIncomeFor() {
        MonthlyIncome income = mockMonthlyIncome();
        income.setId(1L);

        assert income.getId() != null;

        given(
                monthlyIncomeRepository
                        .findById(1L)
        ).willReturn(Optional.of(income));

        incomeExpenseService.deleteMonthlyIncomeFor(1L);

        verify(monthlyIncomeRepository, times(1))
                .findById(any(Long.class));

        verify(monthlyIncomeRepository, times(1))
                .deleteById(income.getId());
    }

    @Test
    void testDeleteWeeklyIncomeFor() {
        Income income = mockWeeklyIncome();
        income.setId(1L);

        assert income.getId() != null;

        given(
                incomeRepository.findById(1L)
        ).willReturn(Optional.of(income));

        incomeExpenseService.deleteWeeklyIncomeFor(1L);

        verify(incomeRepository, times(1))
                .findById(any(Long.class));

        verify(incomeRepository, times(1))
                .deleteById(income.getId());
    }
}