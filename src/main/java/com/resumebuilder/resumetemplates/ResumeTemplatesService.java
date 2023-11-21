package com.resumebuilder.resumetemplates;

import java.security.Principal;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.resumebuilder.DTO.TemplateDto;
import com.resumebuilder.user.User;

public interface ResumeTemplatesService {

	 List<TemplateDto> getAllTemplates();
	 ResumeTemplates addTemplate(ResumeTemplates req,Principal principle);
	 ResumeTemplates updateTemplate(String tempId,ResumeTemplates req,Principal principle);
	 ResumeTemplates getTemplateById(String tempId);		
		boolean deleteTemplate(String tempId, Principal principle);
		public boolean deleteTemplatePerminantly(String tempId);
		
	public ResumeTemplates getGeneratedPreview(ResumeTemplates tem,User u) throws JsonProcessingException ;

	  
}
