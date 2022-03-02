package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.WeeklyExpense;
import com.sadatmalik.fusionweb.repositories.MonthlyExpenseRepository;
import com.sadatmalik.fusionweb.repositories.WeeklyExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExpenseService {

    private final MonthlyExpenseRepository monthlyExpenseRepository;
    private final WeeklyExpenseRepository weeklyExpenseRepository;

    public List<MonthlyExpense> getMonthlyExpensesFor(User user) {
        return monthlyExpenseRepository.findByUser(user);
    }

    public List<WeeklyExpense> getWeeklyExpensesFor(User user) {
        return weeklyExpenseRepository.findByUser(user);
    }

    public MonthlyExpense saveExpense(MonthlyExpense monthlyExpense) {
        return monthlyExpenseRepository.save(monthlyExpense);
    }

}
