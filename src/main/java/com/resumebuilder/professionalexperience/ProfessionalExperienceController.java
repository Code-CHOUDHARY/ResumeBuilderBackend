package com.resumebuilder.professionalexperience;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
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

import com.resumebuilder.exception.ExperienceNotFoundException;
import com.resumebuilder.exception.ProfessionalExperienceException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/professionalexperience")
public class ProfessionalExperienceController {
	
	@Autowired
	private ProfessionalExperienceService experienceService;
	@Autowired
	private ProfessionalExperienceRepository experienceRepository;
	
	// Get an experience by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<ProfessionalExperience> getExperienceById(@PathVariable Long id) {
        try {
            Optional<ProfessionalExperience> experience = experienceService.getExperienceById(id);
            if (experience.isPresent()) {
                return ResponseEntity.ok(experience.get());
            } else {	
                throw new ExperienceNotFoundException("Experience not found.");
            }
        } catch (ExperienceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ProfessionalExperienceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Add a new experience
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<ProfessionalExperience> addExperience( @RequestBody ProfessionalExperience experience,Principal principal) {
        try {
            ProfessionalExperience addedExperience = experienceService.addExperience(experience,principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedExperience);
        } catch (ProfessionalExperienceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } 
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<ProfessionalExperience> updateExperience(@PathVariable Long id, @RequestBody ProfessionalExperience updatedExperience, Principal principal) {
    	try {
            ProfessionalExperience updated = experienceService.updateExperienceById(id, updatedExperience, principal);
            return ResponseEntity.ok(updated);
        } catch (ExperienceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Get all experiences
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<List<ProfessionalExperience>> getAllExperiences() {
        try {
            List<ProfessionalExperience> experiences = experienceService.getAllExperience();
            return ResponseEntity.ok(experiences);
        } catch (ExperienceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ProfessionalExperienceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    } 
    
    /**
     * Get all experiences based on the Employee ID.
     *
     * @param employeeId The Employee ID to filter experiences.
     * @return ResponseEntity with a list of experiences or an error response.
     */
    @GetMapping("/list/{user_id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<List<ProfessionalExperience>> getExperiencesByEmployeeId(@PathVariable String user_id) {
        try {
            List<ProfessionalExperience> experiences = experienceRepository.findByUserId(user_id);
            return ResponseEntity.ok(experiences);
        } catch (ExperienceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ProfessionalExperienceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<String> softDeleteExperience(@PathVariable Long id, Principal principal) {
        try {
            Optional<ProfessionalExperience> experience = experienceService.getExperienceById(id);
            if (experience.isPresent()) {
                ProfessionalExperience experienceToSoftDelete = experience.get();
                experienceToSoftDelete.set_deleted(true);
                experienceService.updateExperienceById(id, experienceToSoftDelete,principal); // Update the 'is_deleted' flag
                return ResponseEntity.ok("Experience has been soft-deleted.");
            } else {
                throw new ExperienceNotFoundException("Experience not found.");
            }
        } catch (ExperienceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Experience not found.");
        } catch (ProfessionalExperienceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
        }
    }

    
}
    
