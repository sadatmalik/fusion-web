package com.sadatmalik.fusionweb.services.websecurity;

import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.repositories.websecurity.UserPrincipalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implements the UserDetailsService interface, specifically the loadUserByUsername(..)
 * method which interacts with the user database tables via the injected userPrincipalRepo.
 *
 * This service is used by the WebSecurity configuration to load user information for the
 * user login authentication process.
 *
 * @see com.sadatmalik.fusionweb.config.WebSecurityConfig
 * @author sadatmalik
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserPrincipalRepo userPrincipalRepo;

    /**
     * Loads a user principal details from the user principal repo, querying by the
     * given username.
     *
     * @param username from the current login.
     * @return the user principal details from the user database.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPrincipalRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + username)
                );
    }
}
