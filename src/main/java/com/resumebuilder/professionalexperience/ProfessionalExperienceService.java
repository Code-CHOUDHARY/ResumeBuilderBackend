package com.resumebuilder.professionalexperience;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface ProfessionalExperienceService {
	

	public String getTotalExperience(String userId);

	public ProfessionalExperience addExperience(ProfessionalExperience experince, Principal principal);
	public List<ProfessionalExperience> getAllExperience();
	public Optional<ProfessionalExperience> getExperienceById(Long id);
	 public ProfessionalExperience updateExperienceById(Long id, ProfessionalExperience updatedExperience);
}

