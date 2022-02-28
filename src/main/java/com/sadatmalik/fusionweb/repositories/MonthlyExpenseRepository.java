package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyExpenseRepository extends CrudRepository<MonthlyExpense, Long> {
    List<MonthlyExpense> findByUser(User user);
}
