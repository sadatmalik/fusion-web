package com.sadatmalik.fusionweb.services;

import brave.ScopedSpan;
import brave.Tracer;
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

    private final Tracer tracer;
    private static final String PEER_SERVICE_NAME = "fusion-mysql";

    private final DebtRepository debtRepository;

    /**
     * Queries the database and returns all stored debt for the given user and traces thd
     * DB call.
     *
     * @param user the user.
     * @return a list of debt objects for the given user.
     */
    public List<Debt> getDebtFor(User user) {
        ScopedSpan newSpan = tracer.startScopedSpan("get-debt-for-user-db-call");
        List<Debt> debts;
        try {
            debts = debtRepository.findAllByUser(user);
            if (debts.isEmpty()) {
                log.warn("Unable to find any debts for user: " + user);
            } else {
                log.debug("Got debts for user: " + user + ": " + debts);
            }
        } finally {
            newSpan.tag("peer.service", PEER_SERVICE_NAME);
            newSpan.annotate("get.debts.by.user");
            newSpan.finish();
        }
        return debts;
    }
}
