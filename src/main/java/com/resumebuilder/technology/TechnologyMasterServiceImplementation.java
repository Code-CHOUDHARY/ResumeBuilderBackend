package com.resumebuilder.technology;


import java.security.Principal;
import java.util.List;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.RoleException;
import com.resumebuilder.exception.TechnologyException;
import com.resumebuilder.exception.TechnologyNotFoundException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class TechnologyMasterServiceImplementation implements TechnologyMasterServcie {
	

	private final Logger logger = LoggerFactory.getLogger(TechnologyMasterServiceImplementation.class);
	
	@Autowired
	private TechnologyMasterRepository technologyMasterRepository;
	
	@Autowired
	private UserRepository userRepository;
	

	/**
     * Add a new technology record.
     *
     * @param technology The technology record to be added.
     * @throws TechnologyException if there is an issue adding the technology.
     * @return The added technology record.
     */
	

	@Transactional
	public TechnologyMaster addTechnology(TechnologyMaster technology, Principal principal) throws TechnologyException {
		try {
			User user = userRepository.findByEmailId(principal.getName());
			
			logger.info("current user name - " +user.getFull_name());
			
			TechnologyMaster existingTechnology = technologyMasterRepository.findByTechnologyName(technology.getTechnology_name());
			if(existingTechnology != null) {
				if(existingTechnology.is_deleted()) {
					existingTechnology.set_deleted(false);
					existingTechnology.setModified_by(user.getFull_name());
					return technologyMasterRepository.save(existingTechnology);
				}else {
					throw new TechnologyException("Technology with the same name already exist.");
				}
			}
			
			TechnologyMaster saveTechnology = new TechnologyMaster();
			saveTechnology.setTechnology_name(technology.getTechnology_name());
			saveTechnology.set_deleted(false);
			saveTechnology.setModified_by(user.getFull_name());
			return technologyMasterRepository.save(technology);
			
		} catch (Exception e) {
			throw new TechnologyException("Technology with the same name already exist.");
		}
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
	public TechnologyMaster updateTechnology(Long id, TechnologyMaster updatedTechnology, Principal principal) throws TechnologyNotFoundException, TechnologyException {
		try {
			User user = userRepository.findByEmailId(principal.getName());
			TechnologyMaster existingTechnology = technologyMasterRepository.findById(id).orElseThrow(() -> new TechnologyException("Technology not found with id: " + id));
			// Check if the role is marked as deleted
	        if (existingTechnology.is_deleted()) {
	            throw new TechnologyException("Cannot update a deleted technology.");
	        }

	        // Update the role properties
	        existingTechnology.setTechnology_name(updatedTechnology.getTechnology_name());
	        existingTechnology.setModified_by(user.getFull_name());

	        return technologyMasterRepository.save(existingTechnology);
		} catch (Exception e) {
			throw new TechnologyException("Technology does not exist.");
		}
		
		
//		try {
//			User user = userRepository.findByEmailId(principal.getName());
//            Optional<TechnologyMaster> optionalTechnology = technologyMasterRepository.findById(id)
//;
//            if (optionalTechnology.isPresent()) {
//            	
//                TechnologyMaster existingTechnology = optionalTechnology.get(); 
//                existingTechnology.setTechnology_name(updatedTechnology.getTechnology_name());
//                existingTechnology.setModified_by(user.getFull_name());
//                return technologyMasterRepository.save(existingTechnology);
//            } else {
//                throw new TechnologyNotFoundException("Technology with ID " + id + " not found.");
//            }
//        } catch (Exception e) {
//            throw new TechnologyException("Technology does not exist");
//        }
	}
	
	/**
     * Delete a technology record by ID.
     *
     * @param id The ID of the technology record to be deleted.
     * @throws TechnologyNotFoundException if the technology with the given ID does not exist.
<<<<<<< HEAD
     * @throws TechnologyException if there is an issue deleting the technology.
=======
     * @throws TechnologyException         if there is an issue deleting the technology.
>>>>>>> b6ed48a0d25b78927d37a643b2e00365629f7edd
     */	

	@Override
	public void deleteTechnology(Long id) throws TechnologyNotFoundException, TechnologyException {
		
			try {

	            Optional<TechnologyMaster> optionalTechnology = technologyMasterRepository.findById(id)
;

	            if (optionalTechnology.isPresent()) {
	                TechnologyMaster existingTechnology = optionalTechnology.get();
	                existingTechnology.set_deleted(true);
	                technologyMasterRepository.save(existingTechnology);
	            } else {
	                throw new TechnologyNotFoundException("Technology with ID " + id + " not found.");
	            }
	        } 
			catch (Exception e) {
	            throw new TechnologyException("Technology does not exist.");   
			}
	
	}

	@Override
	public List<TechnologyMaster> getAllTechnologies() {
		// TODO Auto-generated method stub
		return technologyMasterRepository.findAll();
	}

	

}

