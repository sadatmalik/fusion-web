package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.FusionWebPrototypeApplication;
import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.repositories.DebtRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.sadatmalik.fusionweb.controllers.TestUtils.mockDebt;
import static com.sadatmalik.fusionweb.controllers.TestUtils.mockUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = FusionWebPrototypeApplication.class)
@ActiveProfiles("test")
class DebtServiceTest {

    @Autowired
    private DebtService debtService;

    @MockBean
    private DebtRepository debtRepository;

    private static User user;

    @BeforeAll
    static void setup() {
        user = mockUser();
    }

    @Test
    void testServiceContextLoads() {
        assertThat(debtService).isNotNull();
    }

    @Test
    void getDebtFor() {
        given(
                debtRepository
                        .findAllByUser(mockUser()))
                .willReturn(List.of(mockDebt()));

        assertThat(debtService.getDebtFor(user))
                .containsExactly(mockDebt());

        verify(debtRepository, times(1))
                .findAllByUser(any(User.class));

    }
}