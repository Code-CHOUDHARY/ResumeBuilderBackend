package com.resumebuilder.roles;

import com.resumebuilder.exception.RoleException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RolesServiceImplementationTest {
	
	@Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private RolesServiceImplementation rolesService;

    // Custom data tracking lists
    private List<String> addedData = new ArrayList<>();
    private List<String> updatedData = new ArrayList<>();
    private List<String> deletedData = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void cleanup() {
        rolesRepository.deleteAll();
        // Clear custom data tracking lists after each test
         addedData.clear();
      updatedData.clear();
     deletedData.clear();
    }

    @Test
    public void testAddRole() throws RoleException {
        // Create a real role to be added
        Roles roleToAdd = new Roles();
        roleToAdd.setRole_name("Test Engineer");
        roleToAdd.setModified_by("Ruchi");

        // Call the service method to add the role
        Roles addedRole = rolesService.addRole(roleToAdd, null);

        // Track the added data
        addedData.add("Added role with ID: " + addedRole.getRole_id());

        // Verify and assertions as usual
        assertNotNull(addedRole);
        assertEquals("Test Engineer", addedRole.getRole_name());
        assertEquals("Ruchi", addedRole.getModified_by());
        assertFalse(addedRole.is_deleted());
    }

    @Test
    public void testUpdateRole() throws RoleException {
        // Create an existing role in the real database
        Roles existingRole = new Roles();
        existingRole.setRole_name("Test Engineer");
        existingRole.setModified_by("Ruchi");
        rolesRepository.save(existingRole);

        // Create an updated role with modified data
        Roles updatedRole = new Roles();
        updatedRole.setRole_name("Solution Developer");
        updatedRole.setModified_by("Ruchi");

        // Call the service method to update the role
        Roles result = rolesService.updateRole(existingRole.getRole_id(), updatedRole, null);

        // Track the updated data
        updatedData.add("Updated role with ID: " + existingRole.getRole_id());

        // Verify and assertions as usual
        assertNotNull(result);
        assertEquals("Solution Developer", result.getRole_name());
        assertEquals("Ruchi", result.getModified_by());
    }

    @Test
    public void testDeleteRole() throws RoleException {
        // Create an existing role in the real database
        Roles existingRole = new Roles();
        existingRole.setRole_name("Solution Developer");
        existingRole.setModified_by("Ruchi");
        rolesRepository.save(existingRole);

        // Call the service method to delete the role
        rolesService.deleteRole(existingRole.getRole_id(), null);

        // Track the deleted data
        deletedData.add("Deleted role with ID: " + existingRole.getRole_id());

        // Verify and assertions as usual
        Roles retrievedRole = rolesRepository.findById(existingRole.getRole_id()).orElse(null);
        assertNotNull(retrievedRole);
        assertTrue(retrievedRole.is_deleted());
    }

    
}

