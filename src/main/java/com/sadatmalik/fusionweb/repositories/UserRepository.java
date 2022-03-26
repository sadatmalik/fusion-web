package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The User DAO interface - extends CrudRepository, enabling Spring Data to
 * find this interface and automatically create an implementation for it.
 *
 * The implementation provides the most relevant CRUD methods for standard data
 * access available in a standard DAO.
 *
 * Spring will analyse any methods defined by the UserRepository interface and
 * will try to automatically generate queries from the method names.
 *
 * @see User
 * @see CrudRepository
 * @author sadatmalik
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
