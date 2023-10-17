package com.resumebuilder.technology;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.exception.RoleException;
import com.resumebuilder.exception.TechnologyException;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyMasterController {
	
	@Autowired
	private TechnologyMasterServcie technologyMasterService;
	
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
	
	 @PostMapping("/add")
	    public ResponseEntity<?> addTechnology(@RequestBody TechnologyMaster technology, Principal principal) throws TechnologyException {
	     try {
			TechnologyMaster technologyMaster = technologyMasterService.addTechnology(technology, principal);
			return ResponseEntity.status(HttpStatus.CREATED).body(technologyMaster);
		} catch (TechnologyException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}   
	    }
 
 
 /**
     * Updates an existing technology record by ID.
     *
     * @param technologyId      The ID of the technology record to be updated.
     * @param updatedTechnology The updated technology information.
     * @return The updated technology record.
     * @throws TechnologyNotFoundException if the technology with the given ID does not exist.
     * @throws TechnologyException         if there is an issue updating the technology.
     */
 
    @PutMapping("/edit/{technologyId}")
    public ResponseEntity<?> updateTechnology(@PathVariable Long technologyId, @RequestBody TechnologyMaster updatedTechnology, Principal principal) {
        try {
			TechnologyMaster technologyMaster = technologyMasterService.updateTechnology(technologyId, updatedTechnology, principal);
			if (technologyMaster != null) {
		       return ResponseEntity.status(HttpStatus.OK).body(technologyMaster);
			}else {
				 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Technology not found with id: " + technologyId);
			}
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}   	
    }
    
    /**
     * Deletes a technology record by ID.
     *
     * @param technologyId The ID of the technology record to be deleted.
     * @throws TechnologyNotFoundException if the technology with the given ID does not exist.
     * @throws TechnologyException         if there is an issue deleting the technology.
     */
    
    @DeleteMapping("/delete/{technologyId}")
    public ResponseEntity<?> deleteTechnology(@PathVariable Long technologyId) {
    	try {
    		technologyMasterService.deleteTechnology(technologyId);
    		return ResponseEntity.status(HttpStatus.OK).body("Technology deleted successfully");
        } catch (RoleException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
       
    }
}