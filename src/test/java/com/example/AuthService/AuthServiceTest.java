package com.example.AuthService;

import com.example.AuthService.DTO.TokenWithUser;
import com.example.AuthService.DTO.User;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Exception.GoogleRegistrationMissingException;
import com.example.AuthService.Repository.UserRepository;
import com.example.AuthService.Service.AuthService;
import com.example.AuthService.Utils.AuthUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(DatabaseSetup.class)
public class AuthServiceTest {

    @Autowired
    private AuthService service;

    @MockitoBean
    private AuthUtil util;

    @BeforeAll
    public static void setup(@Autowired UserRepository repository,
                             @Autowired PasswordEncoder passwordEncoder) {

        User user = new User();
        user.setLogin("test");
        user.setEmail("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setGoogleReg(true);
        repository.save(user);

        User user2 = new User();
        user2.setLogin("test2");
        user2.setEmail("test2");
        user2.setPassword(passwordEncoder.encode("test2"));
        user2.setGoogleReg(false);
        repository.save(user2);
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
    public void testSignInBadCredentials() {
        User user = new User();
        user.setLogin("testFail");
        user.setPassword("testFail");
        Assertions.assertThrows(BadCredentialsException.class, () -> service.signIn(user));
    }

    @Test
    public void testSignInOauthSuccess1() {
        Mockito.when(util.getEmail()).thenReturn("test");
        Assertions.assertDoesNotThrow(() -> service.signInOauth());
    }

    @Test
    public void testSignInOauthSuccess2() {
        Mockito.when(util.getEmail()).thenReturn("test3@test.com");
        Mockito.when(util.generatePassword(any(Integer.class))).thenReturn("test3");
        Mockito.when(util.getGivenName()).thenReturn("test3");
        Mockito.when(util.getFamilyName()).thenReturn("test3");
        Mockito.when(util.updateLogin("test3", 0)).thenReturn("test3");
        TokenWithUser result = (TokenWithUser) service.signInOauth();
        Assertions.assertEquals("test3", result.getUser().getLogin());
    }

    @Test
    public void testSignInOauthGoogleRegistrationMissing() {
        Mockito.when(util.getEmail()).thenReturn("test2");
        Assertions.assertThrows(GoogleRegistrationMissingException.class, () -> service.signInOauth());
    }

}
