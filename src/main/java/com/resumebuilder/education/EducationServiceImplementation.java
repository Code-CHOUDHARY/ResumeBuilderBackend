package com.resumebuilder.education;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.user.UserRepository;


@Service
public class EducationServiceImplementation implements EducationService{
	
	@Autowired
	private EducationRepository educationRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	public Education addEducation(Education education, Principal principal) {
		
		Education createEducation = new Education();
		createEducation.setSchool_college(education.getSchool_college());
		createEducation.setDegree(education.getDegree());
		createEducation.setStart_date(education.getStart_date());
		createEducation.setEnd_date(education.getEnd_date());
		
		
		return null;
	}

	@Override
	public Education updateEducation(Long id, Education updateEducation, Principal principal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Education deleteEducation(Long id, Principal principal) {
		// TODO Auto-generated method stub
		return null;
	}

}
