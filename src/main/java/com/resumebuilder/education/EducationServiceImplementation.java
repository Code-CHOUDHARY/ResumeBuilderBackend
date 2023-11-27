package com.resumebuilder.education;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.exception.EducationException;
import com.resumebuilder.professionalexperience.JsonConverter;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;


@Service
public class EducationServiceImplementation implements EducationService{
	
	@Autowired
    private EducationRepository educationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ActivityHistoryService activityHistoryService;
    
    @Override
    public Education addEducation(Education education, Principal principal) {
        try {
            User user = userRepository.findByEmailId(principal.getName());
            // Check if an education entry with the same degree and user ID exists
            Education existingEducation = educationRepository.findByDegreeAndUser(education.getDegree(), user);

            if (existingEducation != null) {
                // Check if it's a soft-deleted entry
                if (existingEducation.is_deleted()) {
                    // Restore the soft-deleted entry
                	Education newEducation = new Education();
                	newEducation.setSchool_college(education.getSchool_college());
                	newEducation.setDegree(education.getDegree());
                	newEducation.setStart_date(education.getStart_date());
                	newEducation.setEnd_date(education.getEnd_date());
                	newEducation.setShow_dates(false);
                	newEducation.setUser(user);


                    // Calculate and set showDuration
                    String showDuration = calculateShowDuration(newEducation.getStart_date(), newEducation.getEnd_date());
                    newEducation.setShow_duration(showDuration);

                    newEducation.setShow_nothing(false);
                    newEducation.setModified_by(user.getUser_id());
                    newEducation.setModified_on(LocalDateTime.now());
                    newEducation.set_deleted(false);
                    
                    ActivityHistory activityHistory = new ActivityHistory();
                 	String newData;
           		try {
           			 newData = JsonConverter.convertToJson(newEducation);
           			 activityHistory.setActivity_type("Add Education");	            
           	         activityHistory.setDescription("Change in education data");
           	         activityHistory.setNew_data(newData);
           	         activityHistory.setUser(user);
           	         activityHistoryService.addActivity(activityHistory, principal);
           		} catch (Exception e) {
           			// TODO Auto-generated catch block
           			e.printStackTrace();
           		}
                    
                    educationRepository.save(newEducation);

                    return newEducation;
                } else {

                    throw new EducationException("Education with the same degree already exist.");
                }
            } else {
            	
            	//create new education entry
            	//Education createEducation = new Education();
            	// Calculate and set showDuration
              String showDuration = calculateShowDuration(education.getStart_date(), education.getEnd_date());
              education.setShow_duration(showDuration);

              education.setModified_by(user.getUser_id());
              education.setModified_on(LocalDateTime.now());
              education.setSchool_college(education.getSchool_college());
              education.setDegree(education.getDegree());
              education.setStart_date(education.getStart_date());
              education.setEnd_date(education.getEnd_date());
              education.setShow_dates(false);
              education.setShow_nothing(false);
              education.setUser(user);
              education.set_deleted(false);
              
              
              ActivityHistory activityHistory = new ActivityHistory();
           	  String newData = "School/College: " + education.getSchool_college() + ". Degree: " + education.getDegree() +  ", Start date: " + education.getStart_date() 
           	   + ", End date: " + education.getEnd_date();
     		try {
     			 activityHistory.setActivity_type("Add Education");	            
     	         activityHistory.setDescription("Change in education data");
     	         activityHistory.setNew_data(newData);
     	         activityHistory.setUser(user);
     	         activityHistoryService.addActivity(activityHistory, principal);
     		} catch (Exception e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
              
              education = educationRepository.save(education);
            	
            	return education;

            }
        } catch (Exception e) {
            throw new EducationException("Education with the same degree already exist.");
        }
    }

    
   


    private String calculateShowDuration(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        int startYear = startCalendar.get(Calendar.YEAR);
        int endYear = endCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);
        int endMonth = endCalendar.get(Calendar.MONTH);

        int years = endYear - startYear;
        int months = endMonth - startMonth;

        if (months < 0) {
            years--;
            months = 12 + months;
        }

        if (years == 0) {
            return months + " month" + (months > 1 ? "s" : "");
        } else if (months == 0) {
            return years + " year" + (years > 1 ? "s" : "");
        } else {
            return years + " year" + (years > 1 ? "s" : "") + " and " + months + " month" + (months > 1 ? "s" : "");
        }
    }
    public Education updateEducation(Long educationId, Education updatedEducation, Principal principal) {
        try {
            // Find the user associated with the authenticated principal
            User user = userRepository.findByEmailId(principal.getName());

            // Find the existing education entry by its ID
            Education existingEducation = educationRepository.findById(educationId).orElse(null);
            
            if (existingEducation == null) {
                throw new EducationException("Education does not exist.");
            }

            // Check if the existing education entry belongs to the current user
            if (!existingEducation.getUser().equals(user)) {
                throw new EducationException("You are not authorized to update this education entry");
            }
            
            String oldData = "School/College: " + existingEducation.getSchool_college() + ". Degree: " + existingEducation.getDegree() +  ", Start date: " + existingEducation.getStart_date() 
      	   + ", End date: " + existingEducation.getEnd_date();
            
            // Update the fields of the existing education entry
            existingEducation.setDegree(updatedEducation.getDegree());
            existingEducation.setSchool_college(updatedEducation.getSchool_college());
            existingEducation.setStart_date(updatedEducation.getStart_date());
            existingEducation.setEnd_date(updatedEducation.getEnd_date());

            // Calculate and set showDuration
            String showDuration = calculateShowDuration(existingEducation.getStart_date(), existingEducation.getEnd_date());
            existingEducation.setShow_duration(showDuration);

            existingEducation.setShow_nothing(updatedEducation.isShow_nothing());

            existingEducation.setModified_by(user.getUser_id());

            existingEducation.setModified_on(LocalDateTime.now());
            
            ActivityHistory activityHistory = new ActivityHistory();
         	  String newData = "School/College: " + updatedEducation.getSchool_college() + ". Degree: " + updatedEducation.getDegree() +  ", Start date: " + updatedEducation.getStart_date() 
         	   + ", End date: " + updatedEducation.getEnd_date();
   		try {
   			 activityHistory.setActivity_type("Add Education");	            
   	         activityHistory.setDescription("Change in education data");
   	         activityHistory.setNew_data(newData);
   	         activityHistory.setOld_data(oldData);
   	         activityHistory.setUser(user);
   	         activityHistoryService.addActivity(activityHistory, principal);
   		} catch (Exception e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
            
            // Save the updated education entry
            existingEducation = educationRepository.save(existingEducation);

            return existingEducation;
        } catch (Exception e) {
            throw new EducationException(e.getMessage());
        }
    }


    @Override
    public Education softDeleteEducation(Long id, Principal principal) {
    	User user = userRepository.findByEmailId(principal.getName());
    	Optional<Education> optionalExistingEducation = educationRepository.findById(id);

        if (optionalExistingEducation.isPresent()) {
            Education existingEducation = optionalExistingEducation.get();
            existingEducation.set_deleted(true);
            existingEducation.setModified_by(user.getUser_id());
            educationRepository.save(existingEducation);
            
            ActivityHistory activityHistory = new ActivityHistory();
            activityHistory.setActivity_type("Delete Education");	            
  	         activityHistory.setDescription("Change in education data");
  	         activityHistory.setNew_data("Education with id " + id + "is deleted");
  	         activityHistory.setUser(user);
  	         activityHistoryService.addActivity(activityHistory, principal);
  
            
            return existingEducation;
        } else {
            throw new EducationException("Education does not exist.");
        }
    }

    
    @Override
    public List<Education> listActiveEducationsForUser(Long userId) {
        return educationRepository.findActiveEducationsForUser(userId);
    }

   public String getLatestEducation(String userId) {
	   String res="";
	   res=educationRepository.findLatestEducation(userId);
	   if(res != null && res=="") {
		   return res;
	   }else {
		   return res;
	   }
   }


}
