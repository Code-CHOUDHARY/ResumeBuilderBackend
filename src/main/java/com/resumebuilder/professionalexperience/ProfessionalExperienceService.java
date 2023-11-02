package com.resumebuilder.professionalexperience;

import java.util.List;
import java.util.Optional;

public interface ProfessionalExperienceService {
	
	public ProfessionalExperience addExperience(ProfessionalExperience experince);
	public List<ProfessionalExperience> getAllExperience();
	public Optional<ProfessionalExperience> getExperienceById(Long id);
}
