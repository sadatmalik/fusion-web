package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.Income;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends CrudRepository<Income, Long> {
}
