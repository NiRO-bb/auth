package com.example.AuthService;

import com.example.AuthService.Controller.AuthController;
import com.example.AuthService.DTO.Token;
import com.example.AuthService.DTO.User;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private final AuthService service = Mockito.mock(AuthService.class);
    private final AuthController controller = new AuthController(service);

    @Test
    public void signUpTestSuccess() {
        Mockito.when(service.signUp(any(User.class))).thenReturn(new User());
        ResponseEntity<?> response = controller.signUp(new User());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void signUpTestNotFound() {
        Mockito.when(service.signUp(any(User.class))).thenThrow(new DataNotFoundException("error"));
        Assertions.assertThrows(DataNotFoundException.class, () -> controller.signUp(new User()));
    }

    @Test
    public void signUpTestFailure() {
        Mockito.when(service.signUp(any(User.class))).thenThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.signUp(new User()));
    }

    @Test
    public void signInTestSuccess() {
        Mockito.when(service.signIn(any(User.class))).thenReturn(new Token());
        ResponseEntity<?> response = controller.signIn(new User());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void signInTestBadCredentials() {
        Mockito.when(service.signIn(any(User.class))).thenThrow(new BadCredentialsException("error"));
        Assertions.assertThrows(BadCredentialsException.class, () -> controller.signIn(new User()));
    }

    @Test
    public void signInTestFailure() {
        Mockito.when(service.signIn(any(User.class))).thenThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.signIn(new User()));
    }

}
