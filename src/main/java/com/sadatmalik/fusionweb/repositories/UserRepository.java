package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
