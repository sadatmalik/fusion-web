package com.sadatmalik.fusionweb.repositories.websecurity;

import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserPrincipal DAO interface - extends JpaRepository, enabling Spring Data to
 * find this interface and automatically create an implementation for it.
 *
 * The implementation provides the most relevant CRUD methods for standard data
 * access available in a standard DAO. Also support paging and sorting, batch
 * delete, and some additional JPA methods.
 *
 * Spring will analyse any methods defined by the UserPrincipalRepo interface and
 * will try to automatically generate queries from the method names.
 *
 * @see UserPrincipal
 * @see JpaRepository
 * @author sadatmalik
 */
@Repository
public interface UserPrincipalRepo extends JpaRepository<UserPrincipal, Long> {
    Optional<UserPrincipal> findByUsername(String username);
}
