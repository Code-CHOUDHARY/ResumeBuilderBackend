package com.resumebuilder.education;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.exception.CustomErrorResponse;
import com.resumebuilder.exception.EducationException;

@RestController
public class EducationController {

	@Autowired
    private EducationService educationService;
	
	//@PreAuthorize("hasAnyRole('USER','MANAGER')")
	@PostMapping("/education/add")
    public ResponseEntity<?> addEducation(@RequestBody Education education, Principal principal) {
        try {
            Education addedEducation = educationService.addEducation(education, principal);
            return ResponseEntity.ok("Education added successfully.");
        } catch (EducationException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }
    }
	
	@PreAuthorize("hasAnyRole('USER','MANAGER')")
	@PutMapping("/editEducation/{educationId}")
    public Education updateEducation(@PathVariable Long educationId, @RequestBody Education updatedEducation, Principal principal) {
        return educationService.updateEducation(educationId, updatedEducation, principal);
    }

	@PreAuthorize("hasAnyRole('USER','MANAGER')")
    @DeleteMapping("/deleteEducation/{educationId}")
    public ResponseEntity<?> softDeleteEducation(@PathVariable Long educationId, Principal principal) {
        try {
            educationService.softDeleteEducation(educationId, principal);
            return new ResponseEntity<>("Education deleted successfully", HttpStatus.OK);
        } catch (EducationException e) {
            return new ResponseEntity<>("Education does not exist.", HttpStatus.NOT_ACCEPTABLE);
        }
    }

	
    @GetMapping("/educationList/{userId}")
    public ResponseEntity<List<Education>> listActiveEducationsForUser(@PathVariable Long userId) {
        List<Education> activeEducations = educationService.listActiveEducationsForUser(userId);
        return new ResponseEntity<>(activeEducations, HttpStatus.OK);
    }

}
