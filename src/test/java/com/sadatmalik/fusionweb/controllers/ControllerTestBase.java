package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.model.User;
import com.sadatmalik.fusionweb.model.websecurity.Authority;
import com.sadatmalik.fusionweb.model.websecurity.RoleEnum;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.websecurity.CustomUserDetailsService;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

/**
 * @author sm@creativefusion.net
 */
public class ControllerTestBase {

    protected UserPrincipal principal;

    @MockBean
    protected CustomUserDetailsService customUserDetailsService;

    protected void createPrincipal() {
        Authority userAuth = Authority.builder()
                .authority(RoleEnum.ROLE_USER).build();
        principal = new UserPrincipal("USER",
                "user", Collections.singletonList(userAuth));

        User user = User.builder()
                .firstName("Sadat")
                .lastName("Malik")
                .email("sm@creativefusion.net")
                .build();

        principal.setUser(user);
    }

}
