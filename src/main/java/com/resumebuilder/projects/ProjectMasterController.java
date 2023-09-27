package com.resumebuilder.projects;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/manager/project")
public class ProjectMasterController {
	
//	@Autowired
//	private ProjectMasterService projectMasterService; // Autowired instance of ProjectMasterService for handling business logic
//	
//	// This method is used to add a new project master record, accessible only by users with the 'Manager' role.
//	@PostMapping("/add")
//	@PreAuthorize("hasRole('Manager')") // Requires the 'Manager' role to access this endpoint
//    public ResponseEntity<ProjectMaster> addRole(@RequestBody ProjectMaster projectMaster, Principal principal) {
//		// Call the ProjectMasterService to add the project master, passing the projectMaster object and principal
//		ProjectMaster savedRole = projectMasterService.addProject(projectMaster,principal);
//		
//		// Return a ResponseEntity with the saved project master and HTTP status code 201 (Created)
//        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
//    }

}
