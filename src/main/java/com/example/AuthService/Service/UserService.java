package com.example.AuthService.Service;

import com.example.AuthService.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serves as repository searcher for AuthenticationProvider in Spring Security.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return repository.findById(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            }
        };
    }

}
