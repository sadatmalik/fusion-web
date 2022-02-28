package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends CrudRepository<Bank, Long> {

}