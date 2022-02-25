package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.Debt;
import com.sadatmalik.fusionweb.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends CrudRepository<Debt, Long> {
    List<Debt> findAllByUser(User user);
}
