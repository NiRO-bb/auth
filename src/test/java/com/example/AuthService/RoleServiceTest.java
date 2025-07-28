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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.nio.file.AccessDeniedException;
import java.util.List;

@SpringBootTest
@ExtendWith(DatabaseSetup.class)
public class RoleServiceTest {

    @Autowired
    private RoleService service;

    @MockitoBean
    private User mockUser;

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

    @Test
    public void testGetRolesSuccess() throws AccessDeniedException {
        Mockito.when(mockUser.getRolesOnly()).thenReturn(List.of("ADMIN"));
        Mockito.when(mockUser.getLogin()).thenReturn("testRole2");
        List<String> list = service.getRoles("testRole2", mockUser);

        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    public void testGetRolesFailure() throws AccessDeniedException {
        Mockito.when(mockUser.getRolesOnly()).thenReturn(List.of("USER"));
        Mockito.when(mockUser.getLogin()).thenReturn("testRole3");
        Assertions.assertThrows(AccessDeniedException.class, () -> service.getRoles("testRole2", mockUser));
    }

}
