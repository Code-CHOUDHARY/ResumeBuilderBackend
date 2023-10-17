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

import com.resumebuilder.downloadtemplate.RolesExcelExporter;
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
	    
	    @PreAuthorize("hasRole('ADMIN')")
	    @PostMapping("/add")
	    public ResponseEntity<?> addRole(@RequestBody Roles role, Principal principal) {
	        try {
	            Roles addedRole = rolesService.addRole(role, principal);
	            return ResponseEntity.status(HttpStatus.CREATED).body(addedRole);
	        } catch (RoleException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	        }
	    }
	    
	    @PreAuthorize("hasRole('ADMIN')")
	    @PutMapping("/edit/{id}")
	    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Roles updatedRole, Principal principal) {
	        try {
	            Roles updated = rolesService.updateRole(id, updatedRole, principal);
	            if (updated != null) {
	                return ResponseEntity.status(HttpStatus.OK).body(updated);
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found with id: " + id);
	            }
	        } catch (RoleException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	        }
	    }
	    
	    @PreAuthorize("hasRole('ADMIN')")
	    @DeleteMapping("/delete/{id}")
	    public ResponseEntity<?> deleteRole(@PathVariable Long id,Principal principal) {
	        try {
	            rolesService.deleteRole(id,principal);
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
    
	    @GetMapping("export/excel")
	    public void exportToExcel(HttpServletResponse response) throws IOException {
	    	
	    	// Set the response content type to indicate an Excel file download
	        response.setContentType("application/octet-stream");
	        
	        // Create a date formatter to include in the filename
	        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	        String currentDateTime = dateFormatter.format(new Date());
	         
	     // Define the content disposition header for download
	        String headerKey = "Content-Disposition";
	        String headerValue = "attachment; filename=roles_" + currentDateTime + ".xlsx";
	        response.setHeader(headerKey, headerValue);
	         
	     // Retrieve the list of roles from the service
	        List<Roles> listroles = rolesService.getAllRoles();
	         
	       RolesExcelExporter excelExporter = new RolesExcelExporter(listroles);
	         
	       // Export the data to the response output stream
	        excelExporter.export(response);    
	    }  
}
