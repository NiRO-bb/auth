package com.example.AuthService.Controller;

import com.example.AuthService.DTO.RoleToSave;
import com.example.AuthService.DTO.User;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Responsible for obtaining and updating user role list.
 */
@RestController
@RequestMapping("/user-roles")
@RequiredArgsConstructor
public class RoleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    private final RoleService service;

    /**
     * Calls service method to update user role list.
     * <p>
     * Can be accessed at URL /user-roles/save.
     * Can throw custom DataNotFoundException if passed login not exist.
     * Can throw RuntimeException if some error occurred while performing.
     *
     * @param roles contains user login and array of strings - new roles
     * @return updated user instance and http OK status
     */
    @Operation(summary = "Update user role list", description = "Removes all user roles, then adds all passed roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role list updated",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User role list not updated - invalid data",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "Roles saving was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody RoleToSave roles) {
        try {
            User user = service.save(roles);
            LOGGER.info("User role list updated {}", roles.desc());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (DataNotFoundException exception) {
            LOGGER.warn("User role list not updated {}; {}}", roles.desc(), exception.getMessage());
            throw new DataNotFoundException(exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error("Roles saving was failed; Error occurred {}", exception.getMessage());
            throw new RuntimeException("Roles saving was failed.");
        }
    }

    /**
     * Calls service method to obtain user role list.
     * <p>
     * Can be accessed at URL /user-roles/{login}.
     * Can throw custom AccessDeniedException if user who trying to send request has not enough authorities.
     * Can throw RuntimeException if some error occurred while performing.
     *
     * @param login login of user whose role list must be returned
     * @param user who send request - received automatically from JWT
     * @return user role list and http status OK
     */
    @Operation(summary = "Retrieve user roles", description = "Returns user role list if access level is enough")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role list received",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                            examples = @ExampleObject(value = "[\"USER\", \"ADMIN\"]"))),
            @ApiResponse(responseCode = "404", description = "User role list not received - permission denied",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "User role list receiving was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @GetMapping("/{login}")
    public ResponseEntity<?> getRoles(@PathVariable("login") String login,
                                      @AuthenticationPrincipal User user) throws AccessDeniedException {
        try {
            List<String> roles = service.getRoles(login, user);
            LOGGER.info("User role list obtained {}", String.format("{ \"login\":\"%s\" }", login));
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (AccessDeniedException exception) {
            LOGGER.warn("User role list not obtained {}; Error occurred {}",
                    String.format("{ \"login\":\"%s\" }", login), exception.getMessage());
            throw new AccessDeniedException(exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error("User role list obtaining was failed; Error occurred {}", exception.getMessage());
            throw new RuntimeException("User role list obtaining was failed.");
        }
    }

}
