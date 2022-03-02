package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.MonthlyIncome;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.WeeklyExpense;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
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

    private final AccountService accountService;

    public List<MonthlyExpense> getMonthlyExpensesFor(User user) {
        return monthlyExpenseRepository.findByUser(user);
    }

    public List<WeeklyExpense> getWeeklyExpensesFor(User user) {
        return weeklyExpenseRepository.findByUser(user);
    }

    public List<MonthlyIncome> getMonthlyIncomeFor(User user) {
        return monthlyIncomeRepository.findByUser(user);
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

}
