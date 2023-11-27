package com.resumebuilder.professionalexperience;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.exception.ExperienceNotFoundException;
import com.resumebuilder.exception.ProfessionalExperienceException;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;
import com.resumebuilder.user.UserService;

@Service
public class ProfessionalExperienceServiceImplementation implements ProfessionalExperienceService {
	
	@Autowired
	private ProfessionalExperienceRepository experienceRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ActivityHistoryService activityHistoryService;
	
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
			
			      ActivityHistory activityHistory = new ActivityHistory();
			   	  String newData = "Job title: " + experience.getJob_title() + ". Organization name: " + experience.getOrganization_name() +  ", Start date: " + experience.getLocation() 
			   	   + ", End date: " + experience.getEnd_date();
					try {
						 activityHistory.setActivity_type("Add Professional Experience");	            
				         activityHistory.setDescription("Change in Professional Experience");
				         activityHistory.setNew_data(newData);
				         activityHistory.setUser(user);
				         activityHistoryService.addActivity(activityHistory, principal);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			
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
	public ProfessionalExperience updateExperienceById(Long id, ProfessionalExperience updatedExperience, Principal principal) throws ExperienceNotFoundException {
		
		User user = userRepository.findByEmail_Id(principal.getName());
		
		ProfessionalExperience existingExperience = experienceRepo.findById(id)
                .orElseThrow(() -> new ExperienceNotFoundException("Experience not found"));
		
		 String oldData = "Job title: " + existingExperience.getJob_title() + ". Organization name: " + existingExperience.getOrganization_name() +  ", Start date: " + existingExperience.getLocation() 
	   	   + ", End date: " + existingExperience.getEnd_date();
		
        // Copy the properties from the updatedExperience to the existingExperience
        existingExperience.setJob_title(updatedExperience.getJob_title());
        existingExperience.setOrganization_name(updatedExperience.getOrganization_name());
        existingExperience.setLocation(updatedExperience.getLocation());
        existingExperience.setStart_date(updatedExperience.getStart_date());
        existingExperience.setEnd_date(updatedExperience.getEnd_date());     
        existingExperience.set_deleted(updatedExperience.is_deleted());
        existingExperience.setModifiedBy(user.getUser_id());
        
        ActivityHistory activityHistory = new ActivityHistory();
   	  String newData = "Job title: " + updatedExperience.getJob_title() + ". Organization name: " + updatedExperience.getOrganization_name() +  ", Start date: " + updatedExperience.getLocation() 
   	   + ", End date: " + updatedExperience.getEnd_date();
		try {
			 activityHistory.setActivity_type("Update Professional Experience");	            
	         activityHistory.setDescription("Change in Professional Experience data");
	         activityHistory.setNew_data(newData);
	         activityHistory.setOld_data(oldData);
	         activityHistory.setUser(user);
	         activityHistoryService.addActivity(activityHistory, principal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return experienceRepo.save(existingExperience);
	}

	@Override
	public String getTotalExperience(String userId) {
		String totalExperience="";
		// check weather the user Exists or not
		try {
			
				Integer exp= experienceRepo.getTotalExperience(userId);
				System.out.println("userId"+userId);
				if(exp != null) {
					
					totalExperience=exp.toString();
				}
			   
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("unable find the user-->"+userId);
			totalExperience="";
		}catch(Exception e) {
			System.out.println("error while counting experienc-->/n"+e);
		}
	   	return totalExperience;
	}
}