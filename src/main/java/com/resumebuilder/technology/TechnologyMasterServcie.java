package com.resumebuilder.technology;

import com.resumebuilder.roles.Roles;

import jakarta.validation.constraints.AssertFalse.List;

/**
 * Service interface for managing technology records.
 */

public interface TechnologyMasterServcie {
	
	/**
     * Add a new technology record.
     *
     * @param technology The technology record to be added.
     * @return The added technology record.
     */
	
	TechnologyMaster addTechnology(TechnologyMaster technology);
	
	/**
     * Update an existing technology record by ID.
     *
     * @param id             The ID of the technology record to be updated.
     * @param updatedTechnology The updated technology information.
     * @return The updated technology record.
     */
	
    TechnologyMaster updateTechnology(Long id, TechnologyMaster updatedTechnology);
    
    /**
     * Delete a technology record by ID.
     *
     * @param id The ID of the technology record to be deleted.
     */
    
    void deleteTechnology(Long id);
    
    java.util.List<TechnologyMaster> getAllTechnologies();
}
