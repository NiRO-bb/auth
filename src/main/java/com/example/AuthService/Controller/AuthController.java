package com.example.AuthService.Controller;

import com.example.AuthService.DTO.Token;
import com.example.AuthService.DTO.User;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Responsible for authentication and authorization requests.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService service;

    /**
     * Calls service method to add new user to database.
     * <p>
     * Can be accessed at URL /auth/signup.
     * Can throw custom DataNotFoundException if passed user instance is incorrect.
     * Can throw RuntimeException if some error occurred while performing.
     *
     * @param user user instance that must be added
     * @return added user instance and http OK status
     */
    @PutMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        try {
            User addedUser = service.signUp(user);
            LOGGER.info("User was added {}", user.desc());
            return new ResponseEntity<>(addedUser, HttpStatus.OK);
        } catch (DataNotFoundException exception) {
            LOGGER.warn("User was not added {}; {}", user.desc(), exception.getMessage());
            throw new DataNotFoundException(exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error("User adding was failed; Error occurred {}", exception.getMessage());
            throw new RuntimeException("User adding was failed.");
        }
    }

    /**
     * Calls service method to look up some user entity in database.
     * <p>
     * Can be accessed at URL /auth/signin.
     * Can throw BadCredentialException if passed user instance is incorrect.
     * Can throw RuntimeException if some error occurred while performing.
     *
     * @param user user instance that must be found
     * @return JWT that store info about authorized user and http status OK
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody User user) {
        try {
            Token token = service.signIn(user);
            LOGGER.info("User logged in {}", String.format("{ \"login\":\"%s\" }", user.getLogin()));
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException exception) {
            LOGGER.warn("User not logged in {}; {}}",
                    String.format("{ \"login\":\"%s\" }", user.getLogin()), exception.getMessage());
            throw new BadCredentialsException("There is no user with passed parameters.");
        } catch (Exception exception) {
            LOGGER.error("User authorization was failed; Error occurred {}", exception.getMessage());
            throw new RuntimeException("User authentification was failed.");
        }
    }

}
