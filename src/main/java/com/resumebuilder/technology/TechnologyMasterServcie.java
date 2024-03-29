package com.resumebuilder.technology;


import java.security.Principal;
import java.util.List;

import com.resumebuilder.DTO.TechnologyDto;
import com.resumebuilder.roles.Roles;


public interface TechnologyMasterServcie {
	

	/**
     * Add a new technology record.
     *
     * @param technology The technology record to be added.
     * @return The added technology record.
     */
	
	TechnologyMaster addTechnology(TechnologyMaster technology, Principal principal);
	
	/**
     * Update an existing technology record by ID.
     *
     * @param id             The ID of the technology record to be updated.
     * @param updatedTechnology The updated technology information.
     * @return The updated technology record.
     */
	
    TechnologyMaster updateTechnology(Long id, TechnologyMaster updatedTechnology, Principal principal);
    
    /**
     * Delete a technology record by ID.
     *
     * @param id The ID of the technology record to be deleted.
     */
    
    void deleteTechnology(Long id, Principal principal);

    
    List<TechnologyDto> getAllTechnologyList();


}
