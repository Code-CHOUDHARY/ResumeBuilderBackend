package com.resumebuilder.roles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.resumebuilder.exception.RoleException;

@RestController
@RequestMapping("/admin/api/roles")
public class RolesController {
	
	 private final RolesService rolesService;  // Autowired instance of RolesService for handling role-related operations
	    
	    @Autowired
	    public RolesController(RolesService rolesService) {
	        this.rolesService = rolesService;
	    }
	    
	    @PostMapping("/add")
	    public ResponseEntity<?> addRole(@RequestBody Roles role) {
	        try {
	            Roles addedRole = rolesService.addRole(role);
	            return ResponseEntity.status(HttpStatus.CREATED).body(addedRole);
	        } catch (RoleException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	        }
	    }
	    
	    @PutMapping("/edit/{id}")
	    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Roles updatedRole) {
	        try {
	            Roles updated = rolesService.updateRole(id, updatedRole);
	            if (updated != null) {
	                return ResponseEntity.status(HttpStatus.OK).body(updated);
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found with id: " + id);
	            }
	        } catch (RoleException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	        }
	    }
	    
	    @DeleteMapping("/delete/{id}")
	    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
	        try {
	            rolesService.deleteRole(id);
	            return ResponseEntity.status(HttpStatus.OK).body("Role deleted successfully");
	        } catch (RoleException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	        }
	    }
	    
	    @GetMapping("/list")
	    public ResponseEntity<List<Roles>> getAllRoles() {
	        List<Roles> roles = rolesService.getAllRoles();
	        return ResponseEntity.status(HttpStatus.OK).body(roles);
	    }
    

}
