package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.model.Debt;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.repositories.DebtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DebtService {

    private final DebtRepository debtRepository;

    public List<Debt> getDebtFor(User user) {
        return debtRepository.findAllByUser(user);
    }
}
