package com.resumebuilder.resumetemplates;

import java.security.Principal;
import java.util.List;

public interface ResumeTemplatesService {

	 List<ResumeTemplates> getAllTemplates();
	 ResumeTemplates addTemplate(ResumeTemplates req,Principal p);
	 ResumeTemplates updateTemplate(String tempId,ResumeTemplates req,Principal p);
	 ResumeTemplates getTemplateById(String tempId);
	 boolean deleteTemplate(String tempId,Principal p);
	 public boolean deleteTemplatePerminantly(String tempId); 
	 public String replaceTemplateData(String TemplateId,String UserId);
}
