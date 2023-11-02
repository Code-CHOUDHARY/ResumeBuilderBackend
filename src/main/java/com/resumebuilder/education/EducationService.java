package com.resumebuilder.education;

import java.security.Principal;
import java.util.List;

public interface EducationService {
	
	public Education addEducation(Education education, Principal principal);
	public Education updateEducation(Long id, Education updateEducation, Principal principal);
	public Education softDeleteEducation(Long id,Principal principal);
	public List<Education> listActiveEducationsForUser(Long userId);

}
