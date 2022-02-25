package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyExpenseRepository extends CrudRepository<MonthlyExpense, Long> {
}
