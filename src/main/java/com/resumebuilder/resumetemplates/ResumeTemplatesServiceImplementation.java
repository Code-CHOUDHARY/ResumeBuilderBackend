package com.resumebuilder.resumetemplates;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.DTO.EmployeeProjectResponceEntity;
import com.resumebuilder.DTO.TemplateDto;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryService;

import com.resumebuilder.exception.ResumeTemplateExceptions;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;
import com.resumebuilder.user.UserService;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ResumeTemplatesServiceImplementation implements ResumeTemplatesService{
    @Autowired
	private UserService userService;
	@Autowired
	private ResumeTemplatesRepository repo;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private ActivityHistoryService activityHistoryService;
	
	 private static final Logger logger = LogManager.getLogger(ResumeTemplatesServiceImplementation.class); 

	@Override
	public List<TemplateDto> getAllTemplates() {
		//repo.findAll()
		List<ResumeTemplates>temp=repo.findAllAvailable();
		
		logger.info("this is the getAllTemplates method");
		   List<TemplateDto> empProj = temp.stream()
		            .map(proj -> {
		            	TemplateDto entity = this.mapper.map(proj, TemplateDto.class);
		                // Fetch user details by ID and set the name
		                User user = this.userService.findUserByIdUser(proj.getModified_by());
		          
		                if (user != null) {
		                	
		                	//System.out.println(user1.getFull_name()+user.getFull_name());
		                	//entity.setAssign_by(user1.getFull_name());
		                    entity.setModified_by(user.getFull_name());
		                }

		                return entity;
		            })
		            .collect(Collectors.toList());
		
		return empProj;
	}

	@Override
	public ResumeTemplates addTemplate(ResumeTemplates req,Principal principle) {
		User user=userrepo.findByEmail_Id(principle.getName());
		ResumeTemplates template=ResumeTemplates.builder()
				                  .template_name(req.getTemplate_name())
				                  .modified_by(user.getUser_id())
				                  .modified_on(new Date())
				                  .projects(req.getProjects())
				                  .profile_summary(req.getProfile_summary())
				                  .professional_experience(req.getProfessional_experience())
				                  .certificates(req.getCertificates())
				                  .is_deleted(false).build();
		ResumeTemplates savedTemplate=this.repo.save(template);	

		if(savedTemplate!=null) {
			
		  	 ActivityHistory activityHistory = new ActivityHistory();
	 		 activityHistory.setActivity_type("Add Template");
	 		 activityHistory.setDescription("Change in Template data");
	 		 activityHistory.setNew_data("New template Added with Name "+savedTemplate.getTemplate_name());
	 		 activityHistoryService.addActivity(activityHistory, principle); 
		}
		return savedTemplate;
	}

	@Override
	public ResumeTemplates updateTemplate(String tempId,ResumeTemplates req,Principal principle) {
		User user=userrepo.findByEmail_Id(principle.getName());
		ResumeTemplates template=getTemplateById(tempId);
		if(template!=null) {
			template.set_deleted(false);
			template.setCertificates(req.getCertificates());
			template.setModified_by(user.getUser_id());
			template.setModified_on(new Date());
			template.setProfessional_experience(req.getProfessional_experience());
			template.setProfile_summary(req.getProfile_summary());
			template.setProjects(req.getProjects());
			template.setTemplate_name(req.getTemplate_name());
			ResumeTemplates updateTemplate=repo.save(template);

			if(updateTemplate!=null) {
				
				 
				  	ActivityHistory activityHistory = new ActivityHistory();
			 		 activityHistory.setActivity_type("Update Template");
			 		 activityHistory.setDescription("Change in Template data");
			 		 activityHistory.setNew_data("Template updated with Name "+updateTemplate.getTemplate_name());
			 		 activityHistoryService.addActivity(activityHistory, principle); 
				}

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
	public boolean deleteTemplate(String tempId, Principal principle) {
		boolean flag=false;
		ResumeTemplates template=getTemplateById(tempId);
		if(template != null) {
			//soft deleting
			template.set_deleted(true);
			repo.save(template);
			flag=true;

			if(flag) {
				
				 
				  	ActivityHistory activityHistory = new ActivityHistory();
			 		 activityHistory.setActivity_type("Delete Template");
			 		 activityHistory.setDescription("Change in Template data");
			 		 activityHistory.setNew_data("Template Deleted with Name "+template.getTemplate_name());
			 		 activityHistoryService.addActivity(activityHistory, principle); 

				}

		}else {
			
			new ResumeTemplateExceptions("Unable to Delete Resume Template");
		}
		return flag;
	}
 
	
	
	
}
