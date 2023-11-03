package com.resumebuilder.professionalexperience;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.ExperienceNotFoundException;
import com.resumebuilder.exception.ProfessionalExperienceException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class ProfessionalExperienceServiceImplementation implements ProfessionalExperienceService {
	
	@Autowired
	private ProfessionalExperienceRepository experienceRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
     * Add a new experience.
     *
     * @param experience The experience to be added.
     * @return The added experience.
     * @throws ProfessionalExperienceException if the experience cannot be added.
     */	
	
	@Override
	public ProfessionalExperience addExperience(ProfessionalExperience experience,Principal principal) {
		
	User user = userRepository.findByEmail_Id(principal.getName());
		
		
		if (experience == null) {
			throw new ProfessionalExperienceException("Professional Experience data is empty.");
		}
		try {
			ProfessionalExperience Proexperince = new ProfessionalExperience();
			Proexperince.setJob_title(experience.getJob_title());
			Proexperince.setOrganization_name(experience.getOrganization_name());
			Proexperince.setLocation(experience.getLocation());
			Proexperince.setStart_date(experience.getStart_date());
			Proexperince.setEnd_date(experience.getEnd_date());  
			Proexperince.setUser(user);
			
			return experienceRepo.save(Proexperince);
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

	@Override
	public ProfessionalExperience updateExperienceById(Long id, ProfessionalExperience updatedExperience) throws ExperienceNotFoundException {
		
		ProfessionalExperience existingExperience = experienceRepo.findById(id)
                .orElseThrow(() -> new ExperienceNotFoundException("Experience not found"));

        // Copy the properties from the updatedExperience to the existingExperience
        existingExperience.setJob_title(updatedExperience.getJob_title());
        existingExperience.setOrganization_name(updatedExperience.getOrganization_name());
        existingExperience.setLocation(updatedExperience.getLocation());
        existingExperience.setStart_date(updatedExperience.getStart_date());
        existingExperience.setEnd_date(updatedExperience.getEnd_date());     
        existingExperience.set_deleted(updatedExperience.is_deleted());
        
		return experienceRepo.save(existingExperience);
	}

}