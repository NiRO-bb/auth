package com.example.AuthService.Controller;

import com.example.AuthService.DTO.Token;
import com.example.AuthService.DTO.User;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Exception.GoogleRegistrationMissingException;
import com.example.AuthService.Service.AuthService;
import com.example.AuthService.Utils.AuthUtil;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final AuthUtil util;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was added",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User was not added - passed user data is incorrect",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "User adding was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @PutMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        try {
            user.setGoogleReg(false);
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in",
                    content = @Content(schema = @Schema(type = "string", example = "some JWT"))),
            @ApiResponse(responseCode = "404", description = "User not logged in - invalid data",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "User adding was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
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

    /**
     * Authorizes users who was authenticated by OAuth2.
     * Uses email value for identify user.
     * Creates new user if passed email not exist in database.
     *
     * @return JWT with user authorities or JWT and user login and password - if new user was created
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in",
                    content = @Content(schema = @Schema(implementation = Token.class))),
            @ApiResponse(responseCode = "404", description = "User authorization was failed - missing google registration",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "User authorization was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @GetMapping("/oauth")
    public ResponseEntity<?> signInOauth() {
        try {
            Token token = service.signInOauth();
            LOGGER.info("User logged in {}", String.format("{ \"email\":\"%s\" }", util.getEmail()));
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (GoogleRegistrationMissingException exception) {
            LOGGER.error("User authorization was failed; Error occurred {}", exception.getMessage());
            throw new GoogleRegistrationMissingException(exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error("User authorization was failed; Error occurred {}", exception.getMessage());
            throw new RuntimeException("User authentication was failed.");
        }
    }

}
