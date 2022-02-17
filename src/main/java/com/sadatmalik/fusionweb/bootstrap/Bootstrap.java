package com.sadatmalik.fusionweb.bootstrap;

import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.AccountType;
import com.sadatmalik.fusionweb.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("dev")
@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        // set up some test accounts
        log.debug("Setting up Bootstrap accounts");

        Account account = Account.builder()
                .accountId("HS345678")
                .type(AccountType.CURRENT)
                .name("HSBC")
                .balance(20000)
                .build();

        Account account2 = Account.builder()
                .accountId("BA123456")
                .type(AccountType.SAVINGS)
                .name("BARC")
                .balance(65000)
                .build();

        accountRepository.save(account);
        accountRepository.save(account2);

    }
}
