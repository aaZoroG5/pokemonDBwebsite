package com.example.capstoneWebsite.security;

import com.example.capstoneWebsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Date;

@Configuration
@EnableWebSecurity
public class SecurityConfig {//this class was created to let us bypass the default security from SpringBoot

    @Autowired
    private UserService userService;

    @Bean//this is encryption for the password and the hash strength can be changed
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean//this method connects the content of the userService to the database
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/login*", "/css/*", "/js/*", "/createAccount", "/signup-process").permitAll()
                        .requestMatchers("/index").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                        .formLogin(login ->
                            login.loginPage("/login")
                                .defaultSuccessUrl("/index")
                                .failureUrl("/login?error=true")
                                .permitAll())
                        .logout(logout -> logout
                            .logoutSuccessUrl("/login?logout=true")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .permitAll()
                            .deleteCookies("JSESSIONID"));
        return http.build();
    }
}
