package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.FusionWebPrototypeApplication;
import com.sadatmalik.fusionweb.model.Account;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.sadatmalik.fusionweb.controllers.TestUtils.mockAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = FusionWebPrototypeApplication.class)
@ActiveProfiles("test")
class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Test
    public void testServiceContextLoads() {
        assertThat(accountService).isNotNull();
    }

    @Test
    void testGetAccountsFor() {
        given(accountRepository
                .findByUser(any(User.class)))
                .willReturn(List.of(mockAccount()));

        assertThat(accountService.getAccountsFor(new User()))
                .containsExactly(mockAccount());

        verify(accountRepository, times(1))
                .findByUser(any(User.class));
    }

    @Test
    void testGetAccountById() {
        given(accountRepository
                .findByAccountId("AB12345678"))
                .willReturn(mockAccount());

        assertThat(accountService.getAccountById("AB12345678"))
                .isEqualTo(mockAccount());

        verify(accountRepository, times(1))
                .findByAccountId(any(String.class));
    }

    @Test
    void testSaveUserAccount() {

        Account account = mockAccount();

        given(accountRepository
                .save(mockAccount()))
                .willReturn(account);

        User user = new User();

        assertThat(accountService.saveUserAccount(user, account))
                .isEqualTo(account);

        assertThat(accountService.saveUserAccount(user, account))
                .hasFieldOrPropertyWithValue("user", user);

        verify(accountRepository, times(2))
                .save(any(Account.class));
    }

}