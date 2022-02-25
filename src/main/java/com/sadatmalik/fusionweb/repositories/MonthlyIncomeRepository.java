package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.MonthlyIncome;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyIncomeRepository extends CrudRepository<MonthlyIncome, Long> {
}
