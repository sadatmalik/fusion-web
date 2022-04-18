package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Debt;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.repositories.DebtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service that encapsulates all methods that a client of the service may call
 * for Crud and other debt related common behaviours and requests.
 *
 * Uses an instance of DebtRepository for database interactions.
 *
 * @author sadatmalik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DebtService {

    private final DebtRepository debtRepository;

    /**
     * Queries the database and returns all stored debt for the given user.
     *
     * @param user the user.
     * @return a list of debt objects for the given user.
     */
    public List<Debt> getDebtFor(User user) {
        return debtRepository.findAllByUser(user);
    }
}
