package com.sadatmalik.fusionweb.config;

import com.sadatmalik.fusionweb.model.websecurity.Authority;
import com.sadatmalik.fusionweb.model.websecurity.RoleEnum;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.websecurity.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

/**
 * This is the primary web security configuration class.
 *
 * It extends the {@code WebSecurityConfigurerAdapter} and provides implementations
 * of the {@code configure(HttpSecurity httpSecurity)} and {@code
 * configure(AuthenticationManagerBuilder auth)} methods.
 *
 * The implementation uses a custom user details service implementation with
 * BCrypt password encoding. This can easily be changed for alternative
 * implementation if required.
 *
 * All authorization and authentication related configuration should be managed
 * and maintained here.
 *
 * @see #configure(HttpSecurity)
 * @see #configure(AuthenticationManagerBuilder)
 *
 * @author sadatmalik
 */
@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    /**
     * Configures the HttpSecurity.
     *
     * All permissions are granted to static resources - css files, images,
     * webjars, favicon, and the h2-console.
     *
     * Note the line {@code httpSecurity.csrf().disable()} and
     * {@code httpSecurity.headers().frameOptions().disable()} are both
     * required in order to enable the h2-console.
     *
     * Form login based user login is enabled to authenticate all other requests.
     * Users attempting to access any other application URL are forwarded to the
     * login page.
     *
     * Logout success is also configured in this method and returns the user
     * to the login screen with an appropriate logout message.
     *
     * @param httpSecurity injected by the Spring context
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                    //CSS, javascript, etc. should always be accessible for all clients
                    .antMatchers("/css/**", "/images/**").permitAll()
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/favicon.ico").permitAll()
                    //h2 available to all
                    .antMatchers("/h2-console/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/login?logout").permitAll();

        // need these to enable h2-console
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

    /**
     * PasswordEncoder bean method will load a PasswordEncoder in to the Spring
     * application context.
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication is configured by this method.
     *
     * Note the use of inMemoryAuthentication enabling 2 test users which can be used
     * for testing and demo purposes without requiring database access.
     *
     * The primary application authentication mechanism is handle by the provision of
     * a CustomUserDetailsService and PasswordEncoder.
     *
     * @param auth the Spring context injects this
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        Authority userAuth = Authority.builder().authority(RoleEnum.ROLE_USER).build();

        auth.inMemoryAuthentication()
                //add a user to the cache with username "admin" password "hi" and ROLE_USER
                .withUser(new UserPrincipal("USER1", passwordEncoder().encode("password"),
                        Collections.singletonList(userAuth)))
                .withUser(new UserPrincipal("USER2", passwordEncoder().encode("password"),
                        Collections.singletonList(userAuth)));

        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}