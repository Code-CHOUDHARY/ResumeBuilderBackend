package com.resumebuilder.professionalexperience;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.resumetemplates.ResumeTemplatesServiceImplementation;
import com.resumebuilder.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfessionalExperienceServiceImplementation implements ProfessionalExperienceService {
	@Autowired
    private UserService userService;
	 
	@Autowired
	private ProfessionalExperienceRepository expRepo;
	
	 private static final Logger logger = LogManager.getLogger(ProfessionalExperienceServiceImplementation.class); 

	public String getTotalExperience(String userId) {
	String totalExperience="";
	// check weather the user Exists or not
	try {
		if(userService.checkUserExists(userId)){
			Integer exp= expRepo.getTotalExperience(userId);
			totalExperience=exp.toString();
		}else {
			logger.info("unable find the user-->"+userId);
		}
	} catch (UserNotFoundException e) {
		// TODO Auto-generated catch block
		logger.info("unable find the user-->"+userId);
		totalExperience="";
	}catch(Exception e) {
		logger.info("error while counting experienc-->/n"+e);
	}
   	return totalExperience;
}
}
