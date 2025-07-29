package com.example.AuthService.Service;

import com.example.AuthService.DTO.Token;
import com.example.AuthService.DTO.Role;
import com.example.AuthService.DTO.TokenWithUser;
import com.example.AuthService.DTO.User;
import com.example.AuthService.DTO.UserInfo;
import com.example.AuthService.DTO.UserRole;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Exception.GoogleRegistrationMissingException;
import com.example.AuthService.Repository.RoleRepository;
import com.example.AuthService.Repository.UserRepository;
import com.example.AuthService.Repository.UserRoleRepository;
import com.example.AuthService.Utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

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
    private final UserInfoService userInfoService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil util;

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
            System.out.println(user.getLogin());
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

    /**
     * Performs user authorization.
     * Checks if user email is unique, then creates and returns token.
     *
     * @return generated token
     */
    @Transactional
    public Token signInOauth() {
        String email = util.getEmail();
        Optional<User> optUser = userRepo.findByEmail(email);
        if (optUser.isPresent()) {
            if (optUser.get().isGoogleReg()) {
                return new Token(jwtService.generateToken(optUser.get()));
            } else {
                throw new GoogleRegistrationMissingException("This account was registered without Google registration");
            }
        } else {
            String login = email.split("@")[0];
            String password = util.generatePassword(10);
            User user = new User(login, password, email, true, new ArrayList<>());
            userInfoService.save(new UserInfo(email, util.getGivenName(), util.getFamilyName()));
            return signUpOauth(user, login, 0);
        }
    }

    /**
     * Creates new user.
     * Tries to make user login unique with postfix value.
     *
     * @param user user must be added to database
     * @param login default login of user
     * @param postfix used to make login unique
     * @return generated JWT with added user info
     */
    private TokenWithUser signUpOauth(User user, final String login, int postfix) {
        try {
            user.setLogin(util.updateLogin(login, postfix));
            User addedUser = signUp(user);
            return new TokenWithUser(jwtService.generateToken(addedUser),
                    new TokenWithUser.OauthUser(util.updateLogin(addedUser.getLogin(), postfix),
                            user.getPassword()));
        } catch (DataNotFoundException exception) {
            return signUpOauth(user, login, postfix++);
        }
    }

}
