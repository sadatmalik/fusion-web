package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.WeeklyExpense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyExpenseRepository extends CrudRepository<WeeklyExpense, Long> {
}
