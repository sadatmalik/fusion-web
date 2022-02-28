package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.repositories.MonthlyExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExpenseService {

    private final MonthlyExpenseRepository monthlyExpenseRepository;

    public List<MonthlyExpense> getMonthlyExpensesFor(User user) {
        return monthlyExpenseRepository.findByUser(user);
    }

}
