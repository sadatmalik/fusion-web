package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.MonthlyIncome;
import com.sadatmalik.fusionweb.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyIncomeRepository extends CrudRepository<MonthlyIncome, Long> {
    List<MonthlyIncome> findByUser(User user);
}
