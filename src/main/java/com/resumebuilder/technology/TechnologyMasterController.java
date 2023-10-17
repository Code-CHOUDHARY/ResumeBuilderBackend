package com.resumebuilder.technology;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.exception.TechnologyException;
import com.resumebuilder.roles.Roles;

/**
 * REST controller for managing technology records.
 */

@RestController
@RequestMapping("api/technologies")
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

 @PostMapping("/add")
    public TechnologyMaster addTechnology(@RequestBody TechnologyMaster technology) throws TechnologyException {
        return technologyMasterService.addTechnology(technology);
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
    public TechnologyMaster updateTechnology(@PathVariable Long technologyId, @RequestBody TechnologyMaster updatedTechnology) {
        return technologyMasterService.updateTechnology(technologyId, updatedTechnology);
    }
    
    /**
     * Deletes a technology record by ID.
     *
     * @param technologyId The ID of the technology record to be deleted.
     * @throws TechnologyNotFoundException if the technology with the given ID does not exist.
     * @throws TechnologyException         if there is an issue deleting the technology.
     */
    
    @DeleteMapping("/delete/{technologyId}")
    public void deleteTechnology(@PathVariable Long technologyId) {
       technologyMasterService.deleteTechnology(technologyId);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<TechnologyMaster>> getAllRoles() {
        List<TechnologyMaster> technologies = technologyMasterService.getAllTechnologies();
        return ResponseEntity.status(HttpStatus.OK).body(technologies);
    }
}
