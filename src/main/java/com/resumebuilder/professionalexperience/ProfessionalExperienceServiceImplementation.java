package com.resumebuilder.professionalexperience;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.ExperienceNotFoundException;
import com.resumebuilder.exception.ProfessionalExperienceException;

@Service
public class ProfessionalExperienceServiceImplementation implements ProfessionalExperienceService {
	
	@Autowired
	private ProfessionalExperienceRepository experienceRepo;
	
	/**
     * Add a new experience.
     *
     * @param experience The experience to be added.
     * @return The added experience.
     * @throws ProfessionalExperienceException if the experience cannot be added.
     */	
	
	@Override
	public ProfessionalExperience addExperience(ProfessionalExperience experience) {
		
		if (experience == null) {
			throw new ProfessionalExperienceException("Professional Experience data is empty.");
		}
		try {
			return experienceRepo.save(experience);
		} catch (Exception e) {
			throw new ProfessionalExperienceException("Error adding experince" + e);
		}
	}
	
	/**
     * Get a list of all experiences.
     *
     * @return A list of all experiences.
     * @throws ExperienceNotFoundException if no experiences are found.
     */
	
	@Override
	public List<ProfessionalExperience> getAllExperience() {
		
		 List<ProfessionalExperience> experiences = experienceRepo.findAll();

	        if (experiences.isEmpty()) {
	            throw new ExperienceNotFoundException("No experiences found.");
	        }
		
		return experiences;
	}

	/**
    * Get an experience by ID.
    *
    * @param id The ID of the experience to retrieve.
    * @return The experience with the specified ID if found, or an empty Optional.
    */
	
	@Override
	public Optional<ProfessionalExperience> getExperienceById(Long id) {
		// TODO Auto-generated method stub
		return experienceRepo.findById(id);
	}

}
