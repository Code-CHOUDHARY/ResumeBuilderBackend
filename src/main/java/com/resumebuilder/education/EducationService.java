package com.resumebuilder.education;

import java.security.Principal;

public interface EducationService {
	
	public Education addEducation(Education education, Principal principal);
	public Education updateEducation(Long id, Education updateEducation, Principal principal);
	public Education deleteEducation(Long id, Principal principal);

}
