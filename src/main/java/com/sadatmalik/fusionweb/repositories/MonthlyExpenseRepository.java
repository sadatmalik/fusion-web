package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import org.springframework.data.repository.CrudRepository;

public interface MonthlyExpenseRepository extends CrudRepository<MonthlyExpense, Long> {
}
