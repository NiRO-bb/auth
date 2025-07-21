package com.example.AuthService;

import com.example.AuthService.DTO.RoleToSave;
import com.example.AuthService.DTO.User;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Repository.UserRepository;
import com.example.AuthService.Service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@ExtendWith(DatabaseSetup.class)
public class RoleServiceTest {

    @Autowired
    private RoleService service;

    @BeforeAll
    public static void setup(@Autowired UserRepository repository,
                             @Autowired PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setLogin("testRole");
        user.setEmail("testRole");
        user.setPassword(passwordEncoder.encode("testRole"));
        repository.save(user);
    }

    @Test
    public void testSaveSuccess() {
        User user = service.save(new RoleToSave("testRole", new String[] {"ADMIN"}));
        Assertions.assertFalse(user.getRolesOnly().contains("USER"));
        Assertions.assertTrue(user.getRolesOnly().contains("ADMIN"));
    }

    @Test
    public void testSaveNotFound() {
        Assertions.assertThrows(DataNotFoundException.class,
                () -> service.save(new RoleToSave("invalid", new String[0])));
    }

}
