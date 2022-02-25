package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findByUser(User user);

    Account findByAccountId(String accountId);
}
