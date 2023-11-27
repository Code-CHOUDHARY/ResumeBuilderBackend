package com.resumebuilder.resumetemplates;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumebuilder.DTO.TemplateDto;
import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.certifications.Certifications;
import com.resumebuilder.education.EducationService;
import com.resumebuilder.exception.ResumeTemplateExceptions;
import com.resumebuilder.placeholders.PlaceholderService;
import com.resumebuilder.professionalexperience.ProfessionalExperience;
import com.resumebuilder.professionalexperience.ProfessionalExperienceService;
import com.resumebuilder.projects.EmployeeProject;
import com.resumebuilder.technologyExpertise.TechnologyExpertise;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;
import com.resumebuilder.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResumeTemplatesServiceImplementation implements ResumeTemplatesService {
	@Autowired
	private UserService userService;
	@Autowired
	private ResumeTemplatesRepository repo;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	ProfessionalExperienceService experienceService;
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private ActivityHistoryService activityHistoryService;
	
	 private static final Logger logger = LogManager.getLogger(ResumeTemplatesServiceImplementation.class); 
	@Autowired
	private PlaceholderService placeholdeService;

	@Autowired
	private EducationService educationService;


	@Override
	public List<TemplateDto> getAllTemplates() {
		// repo.findAll()
		List<ResumeTemplates> temp = repo.findAllAvailable();

		logger.info("this is the getAllTemplates method");
		List<TemplateDto> empProj = temp.stream().map(proj -> {
			TemplateDto entity = this.mapper.map(proj, TemplateDto.class);
			// Fetch user details by ID and set the name
			User user = this.userService.findUserByIdUser(proj.getModified_by());

			if (user != null) {

				// System.out.println(user1.getFull_name()+user.getFull_name());
				// entity.setAssign_by(user1.getFull_name());
				entity.setModified_by(user.getFull_name());
			}

			return entity;
		}).collect(Collectors.toList());

		return empProj;
	}

	@Override
	public ResumeTemplates addTemplate(ResumeTemplates req, Principal principle) {
		User user = userrepo.findByEmail_Id(principle.getName());

		ResumeTemplates template = ResumeTemplates.builder()
				.template_name(StringEscapeUtils.unescapeHtml4(req.getTemplate_name())).modified_by(user.getUser_id())
				.modified_on(new Date()).projects(StringEscapeUtils.unescapeHtml4(req.getProjects()))
				.profile_summary(StringEscapeUtils.unescapeHtml4(req.getProfile_summary()))
				.professional_experience(StringEscapeUtils.unescapeHtml4(req.getProfessional_experience()))
				.certificates(StringEscapeUtils.unescapeHtml4(req.getCertificates())).is_deleted(false).build();
		ResumeTemplates savedTemplate = this.repo.save(template);

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
	public ResumeTemplates updateTemplate(String tempId, ResumeTemplates req, Principal principle) {
		User user = userrepo.findByEmail_Id(principle.getName());
		ResumeTemplates template = getTemplateById(tempId);
		if (template != null) {
			template.set_deleted(false);
			template.setModified_by(user.getUser_id());
			template.setModified_on(new Date());
			template.setProfessional_experience(StringEscapeUtils.unescapeHtml4(req.getProfessional_experience()));
			template.setProfile_summary(StringEscapeUtils.unescapeHtml4(req.getProfile_summary()));
			template.setProjects(StringEscapeUtils.unescapeHtml4(req.getProjects()));
			template.setCertificates(StringEscapeUtils.unescapeHtml4(req.getCertificates()));
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
		} else {
			new ResumeTemplateExceptions("Unable to Update Resume Template");
			return null;
		}
	}

	@Override
	public ResumeTemplates getTemplateById(String tempId) {
		ResumeTemplates template = repo.findById(Long.parseLong(tempId))
				.orElseThrow(() -> new ResumeTemplateExceptions("Template Not Found"));
		return template;
	}

	@Override
	public boolean deleteTemplatePerminantly(String tempId) {
		boolean flag = false;
		ResumeTemplates template = getTemplateById(tempId);
		if (template != null) {
			repo.deleteById(Long.parseLong(tempId));
			flag = true;
		} else {

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

	// template Replacer
	public ResumeTemplates getGeneratedPreview(ResumeTemplates tem, User u) throws JsonProcessingException {
		ObjectMapper parser = new ObjectMapper();
		// parser.writeValueAsString()
		ResumeTemplates preview = new ResumeTemplates();
		StringBuilder profile = new StringBuilder();
		StringBuilder certificates = new StringBuilder();
		StringBuilder projects = new StringBuilder();
		StringBuilder experince = new StringBuilder();

		Map<String, Object> profileMapper = getProfileMapper(u);
		String res1 = replaceUserDetails(tem.getProfile_summary(), profileMapper);
		profile.append(res1);

		for (ProfessionalExperience exp : u.getProfessionalExperiences()) {
			Map<String, Object> mapper = getProfessionMapper(exp);
			String response = replaceUserDetails(tem.getProfessional_experience(), mapper);
			experince.append(response);

		}

		for (EmployeeProject proj : u.getEmployeeProject()) {
			Map<String, Object> mapper = getProjectMapper(proj);
			String response = replaceUserDetails(tem.getProjects(), mapper);
			projects.append(response);
		}
		for (Certifications cert : u.getCertificate()) {
			Map<String, Object> mapper = getCertificateMapper(cert);
			String response = replaceUserDetails(tem.getCertificates(), mapper);
			certificates.append(response);
		}

		preview.setCertificates(certificates.toString());
		preview.setProfessional_experience(experince.toString());
		preview.setProfile_summary(profile.toString());
		preview.setProjects(projects.toString());

		return preview;

	}

	public String replaceUserDetails(String template, Map<String, Object> map) {
		// template.translateEscapes();

		for (Entry<String, Object> entry : map.entrySet()) {
			// System.out.println("entry--->"+entry.getKey()+"-"+
			// template.contains(entry.getKey()));

			String key = entry.getKey();
			String value = (String) entry.getValue();
			// System.out.println(entry.getKey() + "-" + entry.getValue() + "-->" +
			// (entry.getValue()).getClass());
			if (template.contains(key)) {
				if (value == null || value == "") {
					template = template.replaceAll(entry.getKey(), "");
				} else {
					template = template.replaceAll(entry.getKey(), (String) entry.getValue());
				}
			}
		}
		// replacing All placeholder with or not replaced by any cause,it will shown
		// empty
		template = template.replaceAll("<@@.*?@@>", "");
		// removing all extra double Quotes
		template = template.replaceAll("\"", "");
		return template;

	}

	// profil & technology experitise
	private Map<String, Object> getProfileMapper(User u) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> obj = placeholdeService.getallmappedPlaceholders().getProfileMap();

		map.put(obj.get("Academic"), educationService.getLatestEducation(u.getUser_id().toString()));
		// System.out.println("Total Experience "+);
		map.put(obj.get("Years of Experience"), countExperience1(u.getUser_id().toString()));
		map.put(obj.get("Current Role"), u.getCurrent_role());
		Map<String, String> obj2 = placeholdeService.getallmappedPlaceholders().getTechnologyMap();
		TechnologyExpertise tech = u.getTechnologyExpertise();

		if (tech != null) {
			map.put(obj2.get("Proficient in"), tech.getProficient_in());
			map.put(obj2.get("Familiar with"), tech.getFamiliar_with());
			map.put(obj2.get("Databases"), tech.getDatabase_skills());
			map.put(obj2.get("Framework"), tech.getFrameworks());
			map.put(obj2.get("Professional Summary"), u.getProfessional_summary());
		}

//			 map.put(obj.get("Proficient in"),"");
//			 map.put(obj.get("Familiar with"),"");
//			 map.put(obj.get("Databases"),"");
//			 map.put(obj.get("Framework"),"");
//			 map.put(obj.get("Professional Summary"), "");

		return map;
	}

	// experienceMapper
	public Map<String, Object> getProfessionMapper(ProfessionalExperience exp) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (exp != null) {
			Map<String, String> obj = placeholdeService.getallmappedPlaceholders().getExperienceMap();
			// map.put(obj.get("ExperienceDuration"),experienceDuration(exp));
			map.put(obj.get("OrganizationName"), exp.getOrganization_name());
			map.put(obj.get("Location"), exp.getLocation());
			map.put(obj.get("lobTitle"), exp.getJob_title());
		}
		return map;
	}

	// project mapper
	public Map<String, Object> getProjectMapper(EmployeeProject proj) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (proj != null) {

			Map<String, String> obj = placeholdeService.getallmappedPlaceholders().getProjectMap();
			map.put(obj.get("Project"), proj.getProject_title());
			map.put(obj.get("Project Duration"), projectDuration(proj));
			map.put(obj.get("Project URL"), proj.getProject_url());
			map.put(obj.get("Clent Name"), proj.getClient_name());
			map.put(obj.get("Project summary"), proj.getProject_summary());
			map.put(obj.get("Technologies"), proj.getTechnologies());
			map.put(obj.get("TechnologyStack"), proj.getTechnology_stack());
			map.put(obj.get("Roles and Responsibility"), proj.getRoles_and_responsibility());
		}
		return map;
	}

	// certificate mapper
	public Map<String, Object> getCertificateMapper(Certifications cert) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (cert != null) {

			Map<String, String> obj = placeholdeService.getallmappedPlaceholders().getCertificateMap();
			map.put(obj.get("CertificateName"), cert.getCertificate_name());
			map.put(obj.get("CertificateDate"), cert.getCertificate_date());
			map.put(obj.get("CertificatelLink"), cert.getCertificate_url());
		}
		return map;
	}

	// get Total duration
	public String countExperience1(String userId) {
		String totalExperience = "";
		// check weather the user Exists or not
		String months = experienceService.getTotalExperience(userId);

		if (months != null) {
			Period period = Period.ofMonths(Integer.parseInt(months)).normalized();
			if (period.getYears() <= 0) {
				totalExperience = period.getMonths() + " Months";
			} else if (period.getMonths() <= 0) {
				totalExperience = period.getYears() + " Years";
			} else {
				totalExperience = period.getYears() + " Years " + period.getMonths() + " Months";
			}
		}

		return totalExperience;
	}

	public String projectDuration(EmployeeProject proj) {
		// TODO Auto-generated method stub

		Date d1 = proj.getStart_date();
		Date d2 = proj.getEnd_date();
//	    System.out.println("Start Date"+d1);
//	    System.out.println("End Date"+d2);

		String returnString = "";
		if (proj.isShow_nothing()) {
			returnString = "";
		} else if (proj.isShow_dates()) {

			returnString = new SimpleDateFormat("dd/MM/yyyy").format(d1) + " - "
					+ new SimpleDateFormat("dd/MM/yyyy").format(d2);
		} else if (proj.isShow_duration()) {

			long months = (d2.getTime() - d1.getTime()) / (long) (1000L * 60 * 60 * 24 * 30);
			if (months >0) {
				Period period = Period.ofMonths((int)months+3).normalized();
				if (period.getYears() <= 0) {
					returnString = period.getMonths() + " Months";
				} else if (period.getMonths() <= 0) {
					returnString = period.getYears() + " Years";
				} else {
					returnString = period.getYears() + " Years " + period.getMonths() + " Months";
				}
			}

		} else {
			returnString = "";
		}

		return returnString;
	}

	public String experienceDuration(ProfessionalExperience exp) {
		// TODO Auto-generated method stub

		Date d1 = new Date(exp.getStart_date());
		Date d2 = new Date(exp.getEnd_date());
		// System.out.println("Experi"+exp.isShowDates()+"-"+exp.isShowDuration());
		String returnString = "";
		if (exp.isShow_nothing()) {
			returnString = "";
		} else if (exp.isShow_dates()) {

			returnString = new SimpleDateFormat("dd/MM/yyyy").format(d1) + " - "
					+ new SimpleDateFormat("dd/MM/yyyy").format(d2);
		} else if (exp.isShow_duration()) {

			returnString = String.valueOf((d2.getTime() - d1.getTime()) / (long) (1000L * 60 * 60 * 24 * 365))
					+ " Years";
			// System.out.println("duration"+((d2.getTime() - d1.getTime())/(long) ((1000 *
			// 60 * 60 *24))));
		} else {
			returnString = "";
		}

		return returnString;
	}
}
