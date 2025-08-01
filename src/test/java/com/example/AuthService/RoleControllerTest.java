package com.example.AuthService;

import com.example.AuthService.Controller.RoleController;
import com.example.AuthService.DTO.RoleToSave;
import com.example.AuthService.DTO.User;
import com.example.AuthService.Exception.DataNotFoundException;
import com.example.AuthService.Service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {

    private final RoleService service = Mockito.mock(RoleService.class);
    private final RoleController controller = new RoleController(service);

    @Test
    public void testSaveSuccess() {
        Mockito.when(service.save(any(RoleToSave.class))).thenReturn(new User());
        ResponseEntity<?> response = controller.save(new RoleToSave("login", new String[0]));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveNotFound() {
        Mockito.when(service.save(any(RoleToSave.class))).thenThrow(new DataNotFoundException("error"));
        Assertions.assertThrows(DataNotFoundException.class,
                () -> controller.save(new RoleToSave("login", new String[0])));
    }

    @Test
    public void testSaveFailure() {
        Mockito.when(service.save(any(RoleToSave.class))).thenThrow(new DataAccessException("error") {});
        Assertions.assertThrows(DataAccessException.class, () -> controller.save(new RoleToSave()));
    }

    @Test
    public void testGetRolesSuccess() throws AccessDeniedException {
        Mockito.when(service.getRoles(any(String.class), any(User.class))).thenReturn(new ArrayList<>());
        ResponseEntity<?> response = controller.getRoles("login", new User());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetRolesAccessDenied() throws AccessDeniedException {
        Mockito.when(service.getRoles(any(String.class), any(User.class))).thenThrow(new AccessDeniedException("error"));
        Assertions.assertThrows(AccessDeniedException.class, () -> controller.getRoles("login", new User()));
    }

    @Test
    public void testGetRolesFailure() throws AccessDeniedException {
        Mockito.when(service.getRoles(any(String.class), any(User.class))).thenThrow(new DataAccessException("error") {});
        Assertions.assertThrows(DataAccessException.class, () -> controller.getRoles("login", new User()));
    }

}
