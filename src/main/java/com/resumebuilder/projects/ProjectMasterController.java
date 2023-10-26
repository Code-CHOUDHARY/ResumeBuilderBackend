package com.resumebuilder.projects;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.exception.ProjectException;


@RestController
@RequestMapping("/project")
public class ProjectMasterController {
	
	@Autowired
	private ProjectMasterService projectMasterService; // Autowired instance of ProjectMasterService for handling business logic
	
	//Demo- add project for testing assigned project
	@PostMapping("/add")
	@PreAuthorize("hasRole('MANAGER')") // Requires the 'Manager' role to access this endpoint
    public ResponseEntity<ProjectMaster> addProject(@RequestBody ProjectMaster projectMaster, Principal principal) {
	
		ProjectMaster savedRole = projectMasterService.addProject(projectMaster,principal);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }
	
	/**
     * Endpoint for assigning a project to a user.
     * 
     * @param userId     The ID of the user to whom the project will be assigned.
     * @param projectId  The ID of the project to be assigned.
     * @param principal  The Principal object representing the user.
     * @return A response entity with a success message.
     */
	
	@PostMapping("/assign/{userId}/{projectId}")
	@PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> assignProjectToUser(@PathVariable Long userId, @PathVariable Long projectId, Principal principal) {
		try{
			projectMasterService.assignProjectToUser(userId, projectId, principal);
			return ResponseEntity.ok("Project assigned successfully.");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
		}
		
	}
		
	/**
     * Endpoint for editing an assigned project.
     * 
     * @param userId        The ID of the user to whom the project is assigned.
     * @param emp_project_id The ID of the assigned project.
     * @param updatedProject The updated project details.
     * @param principal      The Principal object representing the user.
     * @return A response entity containing the edited project.
     * @throws Exception If an error occurs during editing.
     */

	@PreAuthorize("hasAnyRole('USER','MANAGER')")
	@PutMapping("/editAssignProject/{userId}/{emp_project_id}")
    public ResponseEntity<?> editAssignedProject(@PathVariable Long userId, @PathVariable Long emp_project_id, @RequestBody EmployeeProject updatedProject, Principal principal) throws Exception {
        try {
        	EmployeeProject editedProject = projectMasterService.editAssignedProject(userId, emp_project_id, updatedProject, principal);
        if(editedProject != null) {
        	return ResponseEntity.status(HttpStatus.OK).body(editedProject);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Assign project not found with id: "+emp_project_id);
        }
        }catch(ProjectException e) {
        	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
	
	
	// Soft delete a project and its assignment
    @DeleteMapping("/delete/{projectId}/{emp_project_id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId, @PathVariable Long emp_project_id) {
    	projectMasterService.deleteProjectMasterAndAssignProject(projectId, emp_project_id);
        return ResponseEntity.ok("Project deleted successfully.");
    }
}
