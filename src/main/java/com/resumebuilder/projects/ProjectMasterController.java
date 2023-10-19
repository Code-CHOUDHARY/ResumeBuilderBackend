package com.resumebuilder.projects;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/project")
public class ProjectMasterController {
	
	@Autowired
	private ProjectMasterService projectMasterService; // Autowired instance of ProjectMasterService for handling business logic
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('MANAGER')") // Requires the 'Manager' role to access this endpoint
    public ResponseEntity<ProjectMaster> addProject(@RequestBody ProjectMaster projectMaster, Principal principal) {
	
		ProjectMaster savedRole = projectMasterService.addProject(projectMaster,principal);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

}
