package com.resumebuilder.technology;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.resumebuilder.exception.TechnologyException;
import com.resumebuilder.exception.TechnologyNotFoundException;

@Service
public class TechnologyMasterServiceImplementation implements TechnologyMasterServcie {
	
	@Autowired
	private TechnologyMasterRepository technologyMasterRepository;
	
	/**
     * Add a new technology record.
     *
     * @param technology The technology record to be added.
     * @throws TechnologyException if there is an issue adding the technology.
     * @return The added technology record.
     */
	
	@Override
	public TechnologyMaster addTechnology(TechnologyMaster technology) throws TechnologyException {
		// TODO Auto-generated method stub
		return technologyMasterRepository.save(technology);
	}
	
	 /**
     * Update an existing technology record by ID.
     *
     * @param id The ID of the technology record to be updated.
     * @param updatedTechnology The updated technology information.
     * @throws TechnologyNotFoundException if the technology with the given ID does not exist.
     * @throws TechnologyException if there is an issue updating the technology.
     * @return The updated technology record.
     */

	@Override
	public TechnologyMaster updateTechnology(Long id, TechnologyMaster updatedTechnology) throws TechnologyNotFoundException, TechnologyException {
		
		try {
            Optional<TechnologyMaster> optionalTechnology = technologyMasterRepository.findById(id);
            if (optionalTechnology.isPresent()) {
            	
                TechnologyMaster existingTechnology = optionalTechnology.get(); 
                existingTechnology.setTechnology_name(updatedTechnology.getTechnology_name());
                existingTechnology.setModified_by(updatedTechnology.getModified_by());
                return technologyMasterRepository.save(existingTechnology);
            } else {
                throw new TechnologyNotFoundException("Technology with ID " + id + " not found.");
            }
        } catch (Exception e) {
            throw new TechnologyException("Failed to update technology."+ e);
        }
	}
	
	/**
     * Delete a technology record by ID.
     *
     * @param id The ID of the technology record to be deleted.
     * @throws TechnologyNotFoundException if the technology with the given ID does not exist.
     * @throws TechnologyException         if there is an issue deleting the technology.
     */	

	@Override
	public void deleteTechnology(Long id) throws TechnologyNotFoundException, TechnologyException {
		
			try {
	            Optional<TechnologyMaster> optionalTechnology = technologyMasterRepository.findById(id);
	            if (optionalTechnology.isPresent()) {
	                TechnologyMaster existingTechnology = optionalTechnology.get();
	                existingTechnology.set_deleted(true);
	                technologyMasterRepository.save(existingTechnology);
	            } else {
	                throw new TechnologyNotFoundException("Technology with ID " + id + " not found.");
	            }
	        } 
			catch (Exception e) {
	            throw new TechnologyException("Failed to delete technology."+ e);   
			
			}
	
	}

	@Override
	public List<TechnologyMaster> getAllTechnologies() {
		// TODO Auto-generated method stub
		return technologyMasterRepository.findAll();
	}
}
