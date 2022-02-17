package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
