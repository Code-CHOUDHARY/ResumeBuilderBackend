package com.resumebuilder.technologyExpertise;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.DTO.TechnologyExpertiseDto;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.user.User;

@RestController
@RequestMapping("/api/technology-expertise")
public class TechnologyExpertiseController {
	private final TechnologyExpertiseService technologyExpertiseService;

	@Autowired
	public TechnologyExpertiseController(TechnologyExpertiseService technologyExpertiseService) {
		this.technologyExpertiseService = technologyExpertiseService;
	}


	
	 @PostMapping("/add")
	    public ResponseEntity<Object> addTechnologyExpertise(Principal principal, @RequestBody TechnologyExpertiseDto expertiseDto) {
	        try {
	            // Call the service method to add technology expertise
	            TechnologyExpertiseDto addedTechnologyExpertise = technologyExpertiseService.addTechnologyExpertise(principal, expertiseDto);

	            return ResponseEntity.ok(addedTechnologyExpertise);
	        } catch (UserNotFoundException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	        }
	    }
	
	  
	 /**
	     * Endpoint to fetch technology expertise for a user based on user ID.
	     *
	     * @param userId The ID of the user for whom to fetch technology expertise.
	     * @return ResponseEntity with the list of technology expertise as DTOs if successful,
	     * or an error message if not.
	     */
//	    @GetMapping("/get/{userId}")
//	    public ResponseEntity<Object> getTechnologyExpertiseByUserId(@PathVariable Long userId) {
//	        try {
//	            // Call the service method to fetch technology expertise by user ID
//	            TechnologyExpertiseDto technologyExpertiseDtoList = technologyExpertiseService.getTechnologyExpertiseByUserId(userId);
//
//	            return ResponseEntity.ok(technologyExpertiseDtoList);
//	        } catch (UserNotFoundException e) {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//	        } catch (Exception e) {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
//	        }
//	    }
}

