package com.example.AuthService;

import com.example.AuthService.Service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DatabaseSetup.class)
public class RoleControllerUITest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService service;

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testRoleSavePermission() throws Exception {
        mockMvc.perform(put("/user-roles/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "login": "test",
                            "roles": ["USER"]
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "SUPERUSER")
    public void testRoleSaveDenial() throws Exception {
        mockMvc.perform(put("/user-roles/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testGetRolesPermission() throws Exception {
        mockMvc.perform(get("/user-roles/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testGetRolesDenial() throws Exception {
        mockMvc.perform(get("/user-roles/test"))
                .andExpect(status().is3xxRedirection());
    }

}
