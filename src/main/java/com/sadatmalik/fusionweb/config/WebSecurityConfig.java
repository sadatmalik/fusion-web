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

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/webjars/**");
//    }
}