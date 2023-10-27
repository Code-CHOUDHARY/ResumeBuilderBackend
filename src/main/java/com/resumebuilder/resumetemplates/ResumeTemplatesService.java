package com.resumebuilder.resumetemplates;

import java.util.List;

public interface ResumeTemplatesService {

	 List<ResumeTemplates> getAllTemplates();
	 ResumeTemplates addTemplate(ResumeTemplates req);
	 ResumeTemplates updateTemplate(String tempId,ResumeTemplates req);
	 ResumeTemplates getTemplateById(String tempId);
	 boolean deleteTemplate(String tempId);
	 public boolean deleteTemplatePerminantly(String tempId); 
	 public String replaceTemplateData(String TemplateId,String UserId);
}
