package com.resumebuilder.resumetemplates;

import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.resumebuilder.exception.ResumeTemplateExceptions;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.professionalexperience.ProfessionalExperienceService;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserService;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ResumeTemplatesServiceImplementation implements ResumeTemplatesService{
	
	@Autowired
	private ResumeTemplatesRepository repo;
	@Autowired
    private ProfessionalExperienceService expService;
	 @Autowired
	 private UserService userService;
	
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

	@Override
	public String replaceTemplateData(String TemplateId, String UserId) {
		StringBuilder htmlOutPut=new StringBuilder();
		try {
		ResumeTemplates template=getTemplateById(TemplateId);
		User user=userService.findUserByIdUser(Long.parseLong(UserId));
		//stores each section template as list
		List<String> templateList=List.of(template.getProfile_summary(),template.getProfessional_experience(),template.getProjects(),template.getCertificates());
		Map<String,Object> relationalMap=null;
		
			//get all relations of placeholder-userData
         relationalMap=getReplacerMap(user.toString());
         if(relationalMap.size()>0) {
        	 
        	 for(String templateString:templateList) {
        		 
        		 String out=replacePlaceholder(templateString, relationalMap);
        		 htmlOutPut.append(out);
        	 }
         }else {
        	 new ResumeTemplateExceptions("unable to get User Details");
         }
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return htmlOutPut.toString();
	}
 

	public Map<String,Object> getReplacerMap(String obj) throws JsonMappingException, JsonProcessingException{
//		Gson json=new Gson();
//		 User user = json.fromJson(obj, User.class);
		Map<String,Object> map=new HashMap<String,Object>();
		 try {
			ObjectMapper mapper = new ObjectMapper();
			 mapper.registerModule(new JavaTimeModule());
			 mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			User user=mapper.readValue(obj, User.class);
			
			map.put("<%Name%>", user.getFull_name());
			map.put("<%Education%>","");
			map.put("<%DateOfBirth%>",user.getDate_of_birth());
			map.put("<%CurrentRole%>", user.getCurrent_role());
			map.put("<%Email%>", user.getEmail());
			map.put("<%Gender%>", user.getGender());
			map.put("<%CurrentLocation%>", user.getLocation());
			map.put("<%Mobile%>", user.getMobile_number());
			map.put("<%Projects%>", user.getProjects().toString());
			map.put("<%LinkedIn%>", user.getLinkedin_lnk());
			map.put("<%Blogs%>", user.getBlogs_link());
			map.put("<%Total Experience%>",countExperience(user.getUser_id().toString()));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Error while parsing user object");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 return map;
	}
	
	
	public String countExperience(String userId) {
		String totalExperience="";
		// check weather the user Exists or not
		String months=expService.getTotalExperience(userId);
		Period period = Period.ofMonths( Integer.parseInt(months)).normalized();
		if(period.getYears()<=0) {
			totalExperience=period.getMonths() +" Months";
		}else if(period.getMonths()<=0){
			totalExperience=period.getYears() +" Years";
		}else {
			totalExperience=period.getYears() +" Years "+period.getMonths()+" Months";
		}
		
	   	return totalExperience;
	}
	
	
	public String replacePlaceholder(String template, Map<String, Object> map) {
		
		if (template != null && template != "") {
			for (Entry<String, Object> entry : map.entrySet()) {
				
				String key = entry.getKey();
				String value = (String) entry.getValue();
				System.out.println(key+"------------"+value);
				if (template.contains(key)) {
					if (value == null || value == "") {
						template = template.replaceAll(key, "");
					} else {
						template = template.replaceAll(key, value);
					}
				}
			}
		}
		return template;
	}
	
}
