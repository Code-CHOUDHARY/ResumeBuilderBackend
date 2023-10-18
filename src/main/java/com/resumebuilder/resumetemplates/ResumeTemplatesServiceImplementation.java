package com.resumebuilder.resumetemplates;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.ResumeTemplateExceptions;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ResumeTemplatesServiceImplementation implements ResumeTemplatesService{
	
	@Autowired
	private ResumeTemplatesRepository repo;
	
	 private static final Logger logger = LogManager.getLogger(ResumeTemplatesServiceImplementation.class); 

	@Override
	public List<ResumeTemplates> getAllTemplates() {
		//repo.findAll()
		logger.info("this is the getAllTemplates method");
		return repo.findAllAvailable();
	}

	@Override
	public ResumeTemplates addTemplate(ResumeTemplates req) {
		ResumeTemplates template=ResumeTemplates.builder()
				                  .template_name(req.getTemplate_name())
				                  .modified_by(req.getModified_by())
				                  .modified_on(new Date())
				                  .projects(req.getProjects())
				                  .profile_summary(req.getProfile_summary())
				                  .professional_experience(req.getProfessional_experience())
				                  .certificates(req.getCertificates())
				                  .is_deleted(false).build();
		ResumeTemplates savedTemplate=this.repo.save(template);	
		return savedTemplate;
	}

	@Override
	public ResumeTemplates updateTemplate(String tempId,ResumeTemplates req) {
		ResumeTemplates template=getTemplateById(tempId);
		if(template!=null) {
			template.set_deleted(false);
			template.setCertificates(req.getCertificates());
			template.setModified_by(req.getModified_by());
			template.setModified_on(new Date());
			template.setProfessional_experience(req.getProfessional_experience());
			template.setProfile_summary(req.getProfile_summary());
			template.setProjects(req.getProjects());
			template.setTemplate_name(req.getTemplate_name());
			ResumeTemplates updateTemplate=repo.save(template);
			return updateTemplate;
		}else {
			new ResumeTemplateExceptions("Unable to Update Resume Template");
			return null;
		}
	}

	@Override
	public ResumeTemplates getTemplateById(String tempId) {
		ResumeTemplates template=repo.findById(Long.parseLong(tempId)).orElseThrow(()->new ResumeTemplateExceptions("Template Not Found"));
		return template;
	}

	@Override
	public boolean deleteTemplatePerminantly(String tempId) {
		boolean flag=false;
		ResumeTemplates template=getTemplateById(tempId);
		if(template != null) {
			repo.deleteById(Long.parseLong(tempId));
			flag=true;
		}else {
			
			new ResumeTemplateExceptions("Unable to Delete Resume Template");
		}
		return flag;
	}
 
	@Override
	public boolean deleteTemplate(String tempId) {
		boolean flag=false;
		ResumeTemplates template=getTemplateById(tempId);
		if(template != null) {
			//soft deleting
			template.set_deleted(true);
			repo.save(template);
			flag=true;
		}else {
			
			new ResumeTemplateExceptions("Unable to Delete Resume Template");
		}
		return flag;
	}
 
	
	
	
}
