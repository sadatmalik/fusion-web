package com.sadatmalik.fusionweb.bootstrap;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.AccountType;
import com.sadatmalik.fusionweb.model.Income;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.repositories.AccountRepository;
import com.sadatmalik.fusionweb.repositories.IncomeRepository;
import com.sadatmalik.fusionweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Profile("dev")
@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;

    @Override
    public void run(String... args) throws Exception {
        // set up a test user
        log.debug("Setting up Bootstrap user");

        User user = User.builder()
                .firstName("Sadat")
                .lastName("Malik")
                .email("sadat.malik@me.com")
                .build();

        userRepository.save(user);

        // set up some test accounts
        log.debug("Setting up Bootstrap accounts");

        Account account = Account.builder()
                .accountId("HS345678")
                .type(AccountType.CURRENT)
                .name("HSBC")
                .balance(20000)
                .user(user)
                .build();

        Account account2 = Account.builder()
                .accountId("BA123456")
                .type(AccountType.SAVINGS)
                .name("BARC")
                .balance(65000)
                .user(user)
                .build();

        user.setAccounts(List.of(account, account2));

        accountRepository.save(account);
        accountRepository.save(account2);

        // set up some income data
        log.debug("Setting up Bootstrap income");

        Income income = Income.builder()
                .account(account)
                .amount(new BigDecimal(35))
                .source("Job")
                .weeklyInterval(0)
                .build();

        incomeRepository.save(income);
    }
}
