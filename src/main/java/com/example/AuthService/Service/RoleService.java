package com.example.AuthService.Service;

import com.example.AuthService.DTO.Role;
import com.example.AuthService.DTO.RoleToSave;
import com.example.AuthService.DTO.User;
import com.example.AuthService.DTO.UserRole;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Repository.UserRepository;
import com.example.AuthService.Repository.RoleRepository;
import com.example.AuthService.Repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Responsible for obtaining and updating user role list.
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserRoleRepository urRepo;

    /**
     * Update user role list.
     * <p>
     * Clear user role list, then adds new roles from passed array.
     * Can throw DataNotFoundException if passed login not exist.
     *
     * @param roles contains user login and array of strings - new roles
     * @return updated user instance
     */
    @Transactional
    public User save(RoleToSave roles) {
        Optional<User> optUser = userRepo.findById(roles.getLogin());
        if (optUser.isPresent()) {
            User user = optUser.get();
            urRepo.deleteAllByUserLogin(roles.getLogin());
            user.setRoles(new ArrayList<>());

            for (String role : roles.getRoles()) {
                Optional<Role> optRole = roleRepo.findById(role);
                if (optRole.isPresent()) {
                    UserRole ur = urRepo.save(new UserRole(new UserRole.Key(roles.getLogin(), role),
                            user, optRole.get()));
                    user.getRoles().add(ur);
                }
            }
            return user;
        } else {
            throw new DataNotFoundException("There is no user with such login.");
        }
    }

    /**
     * Obtains role list from database.
     * <p>
     * Checks if user has enough authorities to perform request.
     * Can throw AccessDeniedException if authorities are not enough.
     *
     * @param login login of user whose role list must be returned
     * @param user who send request
     * @return user role list
     */
    public List<String> getRoles(String login, User user) throws AccessDeniedException {
        if (!user.getRolesOnly().contains("ADMIN") && !login.equals(user.getLogin())) {
            throw new AccessDeniedException("Permission denied: your access level is not enough.");
        } else {
            Optional<User> optUser = userRepo.findById(login);
            if (optUser.isPresent()) {
                return optUser.get().getRolesOnly();
            }
            return new ArrayList<>();
        }
    }

}
