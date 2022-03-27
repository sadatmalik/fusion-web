package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The Bank DAO interface - extends CrudRepository, enabling Spring Data
 * to find this interface and automatically create an implementation for it.
 *
 * The implementation provides the most relevant CRUD methods for standard data
 * access available in a standard DAO.
 *
 * Spring will analyse any methods defined by the BankRepository interface and
 * will try to automatically generate queries from the method names.
 *
 * @see Bank
 * @see CrudRepository
 * @author sadatmalik
 */
@Repository
public interface BankRepository extends CrudRepository<Bank, Long> {
}