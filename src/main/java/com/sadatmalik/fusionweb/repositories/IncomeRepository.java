package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.Income;
import com.sadatmalik.fusionweb.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends CrudRepository<Income, Long> {
    List<Income> findByUser(User user);
}
