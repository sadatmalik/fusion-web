package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
import com.sadatmalik.fusionweb.repositories.IncomeRepository;
import com.sadatmalik.fusionweb.repositories.MonthlyExpenseRepository;
import com.sadatmalik.fusionweb.repositories.MonthlyIncomeRepository;
import com.sadatmalik.fusionweb.repositories.WeeklyExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IncomeExpenseService {

    private final MonthlyExpenseRepository monthlyExpenseRepository;
    private final WeeklyExpenseRepository weeklyExpenseRepository;
    private final MonthlyIncomeRepository monthlyIncomeRepository;
    private final IncomeRepository incomeRepository;

    private final AccountService accountService;

    public MonthlyExpense getMonthlyExpenseFor(Long id) {
        return monthlyExpenseRepository.findById(id).orElse(null);
    }

    public List<MonthlyExpense> getMonthlyExpensesFor(User user) {
        return monthlyExpenseRepository.findByUser(user);
    }

    public List<WeeklyExpense> getWeeklyExpensesFor(User user) {
        return weeklyExpenseRepository.findByUser(user);
    }

    public List<MonthlyIncome> getMonthlyIncomeFor(User user) {
        return monthlyIncomeRepository.findByUser(user);
    }

    public List<Income> getWeeklyIncomeFor(User user) {
        return incomeRepository.findByUser(user);
    }

    public MonthlyExpense saveMonthlyExpense(MonthlyExpenseDto monthlyExpenseDto, User user) {
        // @todo use MapStruct
        MonthlyExpense monthlyExpense = MonthlyExpense.builder()
                .name(monthlyExpenseDto.getName())
                .amount(new BigDecimal(monthlyExpenseDto.getAmount()))
                .dayOfMonthPaid(monthlyExpenseDto.getDayOfMonthPaid())
                .type(monthlyExpenseDto.getType())
                .account(accountService.getAccountByAccountId(
                        monthlyExpenseDto.getAccountId())
                )
                .user(user)
                .build();

        return monthlyExpenseRepository.save(monthlyExpense);
    }

    public WeeklyExpense saveWeeklyExpense(WeeklyExpenseDto weeklyExpenseDto, User user) {
        // @todo use MapStruct
        WeeklyExpense weeklyExpense = WeeklyExpense.builder()
                .name(weeklyExpenseDto.getName())
                .amount(new BigDecimal(weeklyExpenseDto.getAmount()))
                .timesPerWeek(weeklyExpenseDto.getTimesPerWeek())
                .type(weeklyExpenseDto.getType())
                .account(accountService.getAccountByAccountId(
                        weeklyExpenseDto.getAccountId())
                )
                .user(user)
                .build();

        return weeklyExpenseRepository.save(weeklyExpense);
    }

    public MonthlyIncome saveMonthlyIncome(MonthlyIncomeDto monthlyIncomeDto, User user) {
        // @todo use MapStruct
        MonthlyIncome monthlyIncome = MonthlyIncome.builder()
                .amount(new BigDecimal(monthlyIncomeDto.getAmount()))
                .source(monthlyIncomeDto.getSource())
                .dayOfMonthReceived(monthlyIncomeDto.getDayOfMonthReceived())
                .account(accountService.getAccountByAccountId(
                        monthlyIncomeDto.getAccountId())
                )
                .user(user)
                .build();

        return monthlyIncomeRepository.save(monthlyIncome);
    }

    public Income saveWeeklyIncome(WeeklyIncomeDto weeklyIncomeDto, User user) {
        // @todo use MapStruct
        Income weeklyIncome = Income.builder()
                .amount(new BigDecimal(weeklyIncomeDto.getAmount()))
                .source(weeklyIncomeDto.getSource())
                .weeklyInterval(weeklyIncomeDto.getWeeklyInterval())
                .account(accountService.getAccountByAccountId(
                        weeklyIncomeDto.getAccountId())
                )
                .user(user)
                .build();

        return incomeRepository.save(weeklyIncome);
    }

    public void deleteMonthlyExpenseFor(Long expenseId) {
        MonthlyExpense toDelete = monthlyExpenseRepository.findById(expenseId).orElse(null);
        if (toDelete != null) {
            monthlyExpenseRepository.deleteById(expenseId);
        } else {
            log.debug("Trying to delete non-existing monthly expense - no such monthly expense with ID - " + expenseId);
        }
        log.debug("Deleted - " + toDelete);
    }
}
