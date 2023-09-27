package com.resumebuilder.roles;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RolesController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RolesControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RolesService rolesService;

    @BeforeEach
    public void setUp() {
        // Define mock behavior for the RolesService methods
        when(rolesService.addRole(any(Roles.class))).thenAnswer(invocation -> {
            Roles role = invocation.getArgument(0);
            // Simulate saving to the database (MySQL)
            role.setRole_id(1L); // Set a mock ID for the added role
            return role;
        });

        when(rolesService.updateRole(Mockito.eq(1L), any(Roles.class))).thenAnswer(invocation -> {
            Roles updatedRole = invocation.getArgument(1);
            // Simulate updating in the database (MySQL)
            updatedRole.setRole_id(1L); // Set a mock ID for the updated role
            return updatedRole;
        });

        when(rolesService.getAllRoles()).thenReturn(new ArrayList<>()); // Mock an empty list for getAllRoles()
    }

    @Test
    public void testAddRole() throws Exception {
        Roles roleToAdd = new Roles();
        roleToAdd.setRole_name("Test Developer");
        roleToAdd.setModified_by("Ruchi");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/api/roles/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleToAdd)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateRole() throws Exception {
        Roles updatedRole = new Roles();
        updatedRole.setRole_name("Solution Developer");
        updatedRole.setModified_by("Ruchi");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/admin/api/roles/edit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRole)))
                .andExpect(status().isOk())
                .andReturn();

        // Validate the response
        String content = result.getResponse().getContentAsString();
        Roles returnedRole = objectMapper.readValue(content, Roles.class);
        assert returnedRole != null;
        assert "Solution Developer".equals(returnedRole.getRole_name());
        assert "Ruchi".equals(returnedRole.getModified_by());
    }

    @Test
    public void testGetAllRoles() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/api/roles/list"))
                .andExpect(status().isOk())
                .andReturn();

        // Validate the response (assuming an empty list for getAllRoles())
        String content = result.getResponse().getContentAsString();
        List<Roles> roles = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, Roles.class));
        assert roles != null;
        assert roles.isEmpty();
    }
}

