package com.example.AuthService;

import com.example.AuthService.DTO.User;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Repository.UserRepository;
import com.example.AuthService.Service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@ExtendWith(DatabaseSetup.class)
public class AuthServiceTest {

    @Autowired
    private AuthService service;

    @BeforeAll
    public static void setup(@Autowired UserRepository repository,
                             @Autowired PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setLogin("test");
        user.setEmail("test");
        user.setPassword(passwordEncoder.encode("test"));
        repository.save(user);
    }

    @Test
    public void testSignUpSuccess() {
        User user = new User();
        user.setLogin("login");
        user.setEmail("email");
        user.setPassword("password");
        user = service.signUp(user);
        Assertions.assertEquals("USER", user.getRolesOnly().getFirst());
    }

    @Test
    public void testSignUpNotFound() {
        User user = new User();
        user.setLogin("test");
        user.setEmail("test");
        user.setPassword("test");
        Assertions.assertThrows(DataNotFoundException.class, () -> service.signUp(user));
    }

    @Test
    public void testSignInSuccess() {
        User user = new User();
        user.setLogin("test");
        user.setPassword("test");
        Assertions.assertDoesNotThrow(() -> service.signIn(user));
    }

    @Test
    public void testSignBadCredentials() {
        User user = new User();
        user.setLogin("test2");
        user.setPassword("test2");
        Assertions.assertThrows(BadCredentialsException.class, () -> service.signIn(user));
    }

}
