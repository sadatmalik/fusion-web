package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
