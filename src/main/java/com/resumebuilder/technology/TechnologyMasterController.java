package com.resumebuilder.technology;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.ResumeBuilderBackendApplication;
import com.resumebuilder.exception.RoleException;
import com.resumebuilder.exception.TechnologyException;
import com.resumebuilder.roles.Roles;

/**
 * REST controller for managing technology records.
 */

@RestController
@RequestMapping("/api/technologies")
public class TechnologyMasterController {

	@Autowired
	private TechnologyMasterServcie technologyMasterService;
	@Autowired
	private TechnologyMasterRepository technologyMasterRepo;
	/**
	 * Adds a new technology record.
	 *
	 * @param technology The technology record to be added.
	 * @return The added technology record.
	 * @throws TechnologyException if there is an issue adding the technology.
	 */

// @PostMapping("/add")
//    public TechnologyMaster addTechnology(@RequestBody TechnologyMaster technology, Principal principal) throws TechnologyException {
//        return technologyMasterService.addTechnology(technology, principal);
//    }

	public static final Logger logger = LoggerFactory.getLogger(TechnologyMasterController.class);

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<?> addTechnology(@RequestBody TechnologyMaster technology, Principal principal)
			throws TechnologyException {
		try {
			TechnologyMaster technologyMaster = technologyMasterService.addTechnology(technology, principal);
			return ResponseEntity.status(HttpStatus.CREATED).body(technologyMaster);
		} catch (TechnologyException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
		}
	}

	/**
	 * Updates an existing technology record by ID.
	 *
	 * @param technologyId      The ID of the technology record to be updated.
	 * @param updatedTechnology The updated technology information.
	 * @return The updated technology record.
	 * @throws TechnologyNotFoundException if the technology with the given ID does
	 *                                     not exist.
	 * @throws TechnologyException         if there is an issue updating the
	 *                                     technology.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/edit/{technologyId}")
	public ResponseEntity<?> updateTechnology(@PathVariable Long technologyId,
			@RequestBody TechnologyMaster updatedTechnology, Principal principal) {
		try {
			TechnologyMaster technologyMaster = technologyMasterService.updateTechnology(technologyId,
					updatedTechnology, principal);
			if (technologyMaster != null) {
				return ResponseEntity.status(HttpStatus.OK).body(technologyMaster);

			}else {
				 return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Technology not found with id: " + technologyId);
			} 
			}
		} catch (Exception e) {

        	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());


		}
	}

	/**
	 * Deletes a technology record by ID.
	 *
	 * @param technologyId The ID of the technology record to be deleted.
	 * @throws TechnologyNotFoundException if the technology with the given ID does
	 *                                     not exist.
	 * @throws TechnologyException         if there is an issue deleting the
	 *                                     technology.
	 */

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{technologyId}")
	public ResponseEntity<?> deleteTechnology(@PathVariable Long technologyId, Principal principal) {
		try {
			technologyMasterService.deleteTechnology(technologyId, principal);
			return ResponseEntity.status(HttpStatus.OK).body("Technology deleted successfully");
		} catch (RoleException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
		}

	}

	 /**
     * Get a list of all technologies.
     *
     * @return A list of technologies.
     */
    
	@GetMapping("/list")
	public ResponseEntity<List<TechnologyMaster>> getAllRoles() {
		java.util.List<TechnologyMaster> technologies = technologyMasterService.getAllTechnologyList();
		return ResponseEntity.status(HttpStatus.OK).body(technologies);
	}

	/**
	 * Retrieves a list of skill suggestions based on user input.
	 *
	 * @param input The user input for skill suggestions (a single character).
	 * @return A list of skill suggestions that match the user's input.
	 */

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/suggestions")
	public List<String> suggestSkills(@RequestParam("input") String input) {
		try {
			if (input.length() == 1) {
				// Perform a case-insensitive search for skills starting with the input
				// character
				List<String> suggestions = technologyMasterRepo.findSkillsStartingWith(input);
				logger.info("Input: {}, Suggestions: {}", input, suggestions);
				return suggestions;
			} else {
				return Collections.emptyList(); // Return an empty list for inputs other than a single character
			}
		} catch (Exception e) {
			logger.error("Error occurred while fetching skill suggestions: {}", e.getMessage());
			throw new RuntimeException("An error occurred while fetching skill suggestions.");
		}
	}
}
