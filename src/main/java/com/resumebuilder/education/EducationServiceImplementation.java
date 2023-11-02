package com.resumebuilder.education;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.resumebuilder.exception.EducationException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;


@Service
public class EducationServiceImplementation implements EducationService{
	
	@Autowired
    private EducationRepository educationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public Education addEducation(Education education, Principal principal) {
        try {
            User user = userRepository.findByEmailId(principal.getName());
            Education newEducation = educationRepository.findByDegree(education.getDegree());

            if (newEducation != null) {
                if (newEducation.is_deleted()) {
                	newEducation.setSchool_college(education.getSchool_college());
                	newEducation.setStart_date(education.getStart_date());
                	newEducation.setEnd_date(education.getEnd_date());
                	newEducation.setShow_dates(false);

                    // Calculate and set showDuration
                    String showDuration = calculateShowDuration(newEducation.getStart_date(), newEducation.getEnd_date());
                    newEducation.setShow_duration(showDuration);

                    newEducation.setShow_nothing(false);
                    newEducation.setModified_by(user.getUser_id());
                    newEducation.setModified_on(LocalDateTime.now());
                    newEducation.set_deleted(false);
                    newEducation.setUser(user);
                    educationRepository.save(newEducation);

                    return newEducation;
                } else {
                    throw new EducationException("Education with the same degree already exists: " + education.getDegree());
                }
            } else {
                education.setModified_by(user.getUser_id());
                education.setModified_on(LocalDateTime.now());
                education.set_deleted(false);
                education.setUser(user);
                // Calculate and set showDuration
                String showDuration = calculateShowDuration(education.getStart_date(), education.getEnd_date());
                education.setShow_duration(showDuration);
                education = educationRepository.save(education);
                return education;
            }
        } catch (Exception e) {
            throw new EducationException(e.getMessage());
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


    @Override
    public Education updateEducation(Long id, Education updateEducation, Principal principal) {
        try {
            User user = userRepository.findByEmailId(principal.getName());
            Optional<Education> optionalExistingEducation = educationRepository.findById(id);

            if (optionalExistingEducation.isPresent()) {
                Education existingEducation = optionalExistingEducation.get();
                
                if (existingEducation.is_deleted()) {
                    throw new EducationException("Education does not exist.");
                }

                // Update the education details
                existingEducation.setSchool_college(updateEducation.getSchool_college());
                existingEducation.setStart_date(updateEducation.getStart_date());
                existingEducation.setEnd_date(updateEducation.getEnd_date());

                // Calculate and set showDuration
                String showDuration = calculateShowDuration(existingEducation.getStart_date(), existingEducation.getEnd_date());
                existingEducation.setShow_duration(showDuration);

                existingEducation.setModified_by(user.getUser_id());
                existingEducation.setModified_on(LocalDateTime.now());

                educationRepository.save(existingEducation);
                return existingEducation;
            } else {
                throw new EducationException("Education does not exist.");
            }
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
            return existingEducation;
        } else {
            throw new EducationException("Education does not exist.");
        }
    }

    
    @Override
    public List<Education> listActiveEducationsForUser(Long userId) {
        return educationRepository.findActiveEducationsForUser(userId);
    }




}
