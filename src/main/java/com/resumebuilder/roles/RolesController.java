package com.resumebuilder.roles;

import java.security.Principal;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.DTO.RolesDto;
import com.resumebuilder.exception.RoleException;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin/api/roles")
public class RolesController {
	
	 private final RolesService rolesService;  // Autowired instance of RolesService for handling role-related operations
	    
	    @Autowired
	    public RolesController(RolesService rolesService) {
	        this.rolesService = rolesService;
	    }
	    
	    /**
	     * Add a new role.
	     *
	     * @param role      The role to be added.
	     * @param principal Represents user identity.
	     * @return The added role or an error message.
	     */
	    
	    @PreAuthorize("hasRole('ADMIN')")
	    @PostMapping("/add")
	    public ResponseEntity<?> addRole(@RequestBody Roles role, Principal principal) {
	        try {
	            Roles addedRole = rolesService.addRole(role, principal);
	            return ResponseEntity.status(HttpStatus.CREATED).body(addedRole);
	        } catch (RoleException e) {
	            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
	        }
	    }
	    
	    /**
	     * Update an existing role by ID.
	     *
	     * @param id         The ID of the role to be updated.
	     * @param updatedRole The updated role details.
	     * @param principal  Represents user identity.
	     * @return The updated role or an error message.
	     */
	    
	    @PreAuthorize("hasRole('ADMIN')")
	    @PutMapping("/edit/{id}")
	    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Roles updatedRole, Principal principal) {
	        try {
	            Roles updated = rolesService.updateRole(id, updatedRole, principal);
	            if (updated != null) {
	                return ResponseEntity.status(HttpStatus.OK).body(updated);
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Role not found with id: " + id);
	            }
	        } catch (RoleException e) {
	            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
	        }
	    }
	    
	    /**
	     * Delete a role by ID.
	     * @param id        The ID of the role to be deleted.
	     * @param principal Represents user identity.
	     * @return A success message or an error message.
	     */
	    
	    @PreAuthorize("hasRole('ADMIN')")
	    @DeleteMapping("/delete/{id}")
	    public ResponseEntity<?> deleteRole(@PathVariable Long id,Principal principal) {
	        try {
	            rolesService.deleteRole(id,principal);
	            return ResponseEntity.status(HttpStatus.OK).body("Role deleted successfully");
	        } catch (RoleException e) {
	            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
	        }
	    }
	    
	    /**
	     * Get a list of all roles.
	     * @return A list of roles.
	     */
	    
	    @GetMapping("/list")
	    public ResponseEntity<List<RolesDto>> getAllRoles() {
	        List<RolesDto> roles = rolesService.getAllRoles();
	        return ResponseEntity.status(HttpStatus.OK).body(roles);
	    }
    
	    
}
