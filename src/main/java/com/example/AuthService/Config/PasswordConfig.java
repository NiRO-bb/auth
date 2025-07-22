package com.example.AuthService.Config;

import com.example.AuthService.Password.CustomPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Contains elements describing some security (password) configurations.
 */
@Configuration
public class PasswordConfig {

    /**
     * Set custom password encoder instance.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

}
