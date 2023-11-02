package com.resumebuilder.professionalexperience;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
	
	// Get an experience by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProfessionalExperience> addExperience(@Valid @RequestBody ProfessionalExperience experience,Principal principal) {
        try {
            ProfessionalExperience addedExperience = experienceService.addExperience(experience,principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedExperience);
        } catch (ProfessionalExperienceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } 
    }
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProfessionalExperience> updateExperience(@PathVariable Long id, @Valid @RequestBody ProfessionalExperience updatedExperience) {
    	try {
            ProfessionalExperience updated = experienceService.updateExperienceById(id, updatedExperience);
            return ResponseEntity.ok(updated);
        } catch (ExperienceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Get all experiences
    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
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
}
    
