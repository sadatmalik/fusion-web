package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.WeeklyExpense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeeklyExpenseRepository extends CrudRepository<WeeklyExpense, Long> {
    List<WeeklyExpense> findByUser(User user);
}
