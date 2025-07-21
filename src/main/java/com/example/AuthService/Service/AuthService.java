package com.example.AuthService.Service;

import com.example.AuthService.DTO.Token;
import com.example.AuthService.DTO.Role;
import com.example.AuthService.DTO.User;
import com.example.AuthService.DTO.UserRole;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Repository.RoleRepository;
import com.example.AuthService.Repository.UserRepository;
import com.example.AuthService.Repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Responsible for saving new user instance and checking existing of it.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserRoleRepository urRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates new user with default role.
     * <p>
     * Adds new user entity in database (with hashed password).
     * Adds new role to added user - "USER" role.
     * Can throw DataNotFoundException if passed user already exist.
     *
     * @param user instance that must be added
     * @return added user instance
     */
    public User signUp(User user) {
        boolean isExist = userRepo.existsByLoginOrEmail(user.getLogin(), user.getEmail());
        if (!isExist) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);

            Role role = roleRepo.findById("USER").get();
            UserRole ur = urRepo.save(new UserRole(new UserRole.Key(user.getLogin(), "USER"), user, role));
            user.getRoles().add(ur);
            return user;
        } else {
            throw new DataNotFoundException("Login and email must be unique.");
        }
    }

    /**
     * Checks if passed user instance exist in database.
     *
     * @param user entity that must be found
     * @return generated JWT with authorized user info
     * @throws BadCredentialsException if passed user not exist in database
     */
    public Token signIn(User user) throws BadCredentialsException {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
        User authUser = (User) auth.getPrincipal();
        return new Token(jwtService.generateToken(authUser));
    }

}
