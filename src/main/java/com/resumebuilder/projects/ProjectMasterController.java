package com.resumebuilder.projects;


import java.security.Principal;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.DTO.APIResponse;
import com.resumebuilder.DTO.ProjectDto;
import com.resumebuilder.exception.ProjectException;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;


@RestController
@RequestMapping("/project")
public class ProjectMasterController {
	
	@Autowired
	private ProjectMasterService projectMasterService; // Autowired instance of ProjectMasterService for handling business logic
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private ProjectMasterServiceImplementation projectMasterServiceImplementation;
	
	@GetMapping("/employeesList")
    public List<User> getEmployees() {
    	List<User> employees = userRepository.findEmployees();
        return employees;
    }
	
	
	//Demo- add project for testing assigned project
	@PostMapping("/add")
	@PreAuthorize("hasRole('MANAGER')") // Requires the 'Manager' role to access this endpoint
    public ResponseEntity<ProjectMaster> addProject(@RequestBody ProjectMaster projectMaster, Principal principal) {
	
		ProjectMaster savedRole = projectMasterService.addProject(projectMaster, principal);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
   
	}
	@PutMapping("/updateProject/{projectId}")
	@PreAuthorize("hasRole('MANAGER')")
public ResponseEntity<APIResponse> updateProject(@RequestBody ProjectMasterResponce request,@PathVariable(name = "projectId")Long id,Principal principal){
		
		ProjectMaster project=	this.projectMasterService.updateproject(request, id, principal);
if(project!=null) {
	 return new ResponseEntity<>(new APIResponse(HttpStatus.CREATED,"Project Data", "Updated Succesfully"), HttpStatus.CREATED);
}
else {
	 return new ResponseEntity<>(new APIResponse(HttpStatus.NOT_FOUND,"Project Data", "Not Found"), HttpStatus.NOT_FOUND);
}
}
	@PostMapping("/deletProject/{projectId}")
	@PreAuthorize("hasRole('MANAGER')")
public ResponseEntity<String> deleteProject(@PathVariable(name = "projectId")Long id){
		String project=	this.projectMasterService.deleteproject(id);

	 return new ResponseEntity<>(project, HttpStatus.ACCEPTED);

}
	@GetMapping("/getProjects")
	@PreAuthorize("hasRole('MANAGER')")
public ResponseEntity<List<ProjectMaster>> getProjects(){
		List<ProjectMaster>project=this.projectMasterService.getProjectdata();

		if(project!=null) {
			 return new ResponseEntity<>(project, HttpStatus.ACCEPTED);
		}
		else {
			 return new ResponseEntity<>(project, HttpStatus.NOT_FOUND);
		}

}
	
	@PostMapping("/saveorupdate/employeeproject")
    public ProjectMaster saveOrUpdateProject(@RequestBody ProjectDto projectDTO, Principal principal) {
        return projectMasterService.saveOrUpdateEmployeeProject(projectDTO, principal);
    }
	
	
	/**
     * Endpoint for delete an assigned project.
     * 
     * @param projectId        The ID of the project used for delete project in manager side.
     * @param emp_project_id The ID of the assigned project used for delete assign project.
     * @param principal      The Principal object representing the user.
     * @return A response entity containing the edited project.
     * @throws Exception If an error occurs during editing.
     */
	
	// Soft delete a project and its assignment
    @DeleteMapping("/delete/{emp_project_id}")
    public ResponseEntity<String> deleteAssignProject(@PathVariable Long emp_project_id, Principal principal) {
    	projectMasterService.deleteAssignProjectByEmployee(emp_project_id, principal);
        return ResponseEntity.ok("Project deleted successfully.");
    }
    
    
    //list of assign projects 
//    @GetMapping("/employeeProjectsList/{userId}")
//    public List<EmployeeProject> getAssignedProjectsByUserId(@PathVariable Long userId) {
//        return projectMasterServiceImplementation.getAssignedProjectsByUserId(userId);
//    }
//    
	
}
