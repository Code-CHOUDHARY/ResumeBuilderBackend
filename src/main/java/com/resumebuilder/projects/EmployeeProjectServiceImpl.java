package com.resumebuilder.projects;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.resumebuilder.DTO.EmployeProjectRequestEntity;
import com.resumebuilder.DTO.EmployeeProjectResponceEntity;
import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.professionalexperience.JsonConverter;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;
import com.resumebuilder.user.UserService;

import io.jsonwebtoken.lang.Objects;
@Service
public class EmployeeProjectServiceImpl implements EmployProjectService{
    @Autowired
	private UserService userService;
	@Autowired
	private EmployeeProjectRepository projectRepo;
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private ActivityHistoryService activityHistoryService;
	
	@Override
	public EmployeeProject addEmployeeProject(EmployeProjectRequestEntity projects,Long id,Principal principal) {

         User user=this.userService.findUserByIdUser(id);
     	User user1User=userrepo.findByEmail_Id(principal.getName());
      //	System.out.println(user1User);
	//	System.out.println(user);
		EmployeeProject project=new EmployeeProject();	
		project.setProject_title(projects.getProject_title());
		project.setStart_date(projects.getStart_date());
		project.setEnd_date(projects.getEnd_date());
		project.setCurrent(projects.isCurrent());
		project.setShow_dates(projects.isShow_dates());
		project.setShow_duration(projects.isShow_duration());
		project.setShow_nothing(projects.isShow_nothing());
		project.setProject_url(projects.getProject_url());
		project.setClient_name(projects.getClient_name());
		project.setOrganization_name(projects.getOrganization_name());
		project.setProject_summary(projects.getProject_summary());   
		project.setTechnologies(projects.getTechnologies());
		project.setTechnology_stack(projects.getTechnology_stack());
		project.setRoles_and_responsibility(projects.getRoles_and_responsibility());
		//project.setAssign_by(projects.getAssign_by());
		project.setModified_by(user1User.getUser_id());
	      project.setUser(user);    
	      
	      try {
	    	  String newData = JsonConverter.convertToJson(project);

				ActivityHistory activityHistory = new ActivityHistory();
	            activityHistory.setActivity_type("Add Project");
	            activityHistory.setDescription("Change in Project data");
	            activityHistory.setNew_data(newData);
	            activityHistory.setUser(user);
	            activityHistoryService.addActivity(activityHistory, principal);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
	      
			 return this.projectRepo.save(project);
	
		}

	@Override
	public EmployeeProject updateEmployeeProject(EmployeProjectRequestEntity projects, Long project_id,Principal principal) {
		// TODO Auto-generated method stub
		//List<User>users=this.userRepo.findByIdIn(projects.getUserids());
			EmployeeProject project=this.projectRepo.findByIsDeletedAndId(false, project_id);
			User user1User=this.userrepo.findByEmail_Id(principal.getName());
			
//			project.setProject_title(projects.getProjectTitle());
//			project.setStart_date(projects.getStartDate());
//			project.setEnd_date(projects.getEndDate());
//			project.setCurrent(projects.isCurrent());
//			project.setShow_dates(projects.isShowDates());
//			project.setShow_duration(projects.isShow_duration());
//			project.setShow_nothing(projects.isShow_nothing());
//			project.setProject_url(projects.getProject_url());
//			project.setClient_name(projects.getProject_url());
//			project.setOrganization_name(projects.getOrganization_name());
//			project.setProject_summary(projects.getProject_summary());   
//			project.setTechnologies(projects.getTechnology_stack());
//			project.setTechnology_stack(projects.getTechnology_stack());
//			project.setRoles_and_responsibility(projects.getRoles_and_responsibility());
//			//project.setAssign_by(projects.getAssign_by());
//			project.setModified_by(user1User.getUser_id());
			if (projects.getProject_title() != null) {
		        project.setProject_title(projects.getProject_title());
		    }

		    if (projects.getStart_date() != null) {
		        project.setStart_date(projects.getStart_date());
		    }

		    if (projects.getEnd_date() != null) {
		        project.setEnd_date(projects.getEnd_date());
		    }
		     project.setCurrent(projects.isCurrent());
		       project.setShow_dates(projects.isShow_dates());
		        project.setShow_duration(projects.isShow_duration());
		        project.setShow_nothing(projects.isShow_nothing());
		    if (projects.getProject_url() != null) {
		        project.setProject_url(projects.getProject_url());
		    }

		    if (projects.getClient_name() != null) {
		        project.setClient_name(projects.getClient_name());
		    }

		    if (projects.getOrganization_name() != null) {
		        project.setOrganization_name(projects.getOrganization_name());
		    }

		    if (projects.getProject_summary() != null) {
		        project.setProject_summary(projects.getProject_summary());
		    }

		    if (projects.getTechnology_stack() != null) {
		        project.setTechnologies(projects.getTechnologies());
		        project.setTechnology_stack(projects.getTechnology_stack());
		    }

		    if (projects.getRoles_and_responsibility() != null) {
		        project.setRoles_and_responsibility(projects.getRoles_and_responsibility());
		    }
		    
		    // Compare the fields and identify changes
	        
			Map<String, String> projectChanges = new HashMap<>();

			if (!Objects.nullSafeEquals(projects.getProject_title(), project.getProject_title())) {
			    projectChanges.put("project_title", projects.getProject_title());
			}
			if (!Objects.nullSafeEquals(projects.getProject_url(), project.getProject_url())) {
			    projectChanges.put("project_url", projects.getProject_url());
			}
			if (!Objects.nullSafeEquals(projects.getClient_name(), project.getClient_name())) {
			    projectChanges.put("client_name", projects.getClient_name());
			}
			if (!Objects.nullSafeEquals(projects.getOrganization_name(), project.getOrganization_name())) {
			    projectChanges.put("organization_name", projects.getOrganization_name());
			}
			if (!Objects.nullSafeEquals(projects.getProject_summary(), project.getProject_summary())) {
			    projectChanges.put("project_summary", projects.getProject_summary());
			}
			if (!Objects.nullSafeEquals(projects.getTechnology_stack(), project.getTechnology_stack())) {
			    projectChanges.put("technology_stack", projects.getTechnology_stack());
			}
			if (!Objects.nullSafeEquals(projects.getRoles_and_responsibility(), project.getRoles_and_responsibility())) {
			    projectChanges.put("roles_and_responsibility", projects.getRoles_and_responsibility());
			}
			
			try {
				String newData = JsonConverter.convertToJson(projectChanges);
				String oldData = JsonConverter.convertToJson(project);

				ActivityHistory activityHistory = new ActivityHistory();
	            activityHistory.setActivity_type("Update Project");
	            activityHistory.setDescription("Change in Project data");
	            activityHistory.setOld_data(oldData);
	            activityHistory.setNew_data(newData);
	            activityHistory.setUser(user1User);
	            activityHistoryService.addActivity(activityHistory, principal);
				
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       
		return this.projectRepo.save(project);
	}

	@Override
	public String deleteEmployeeProject(Long project_id) {
		try {
		EmployeeProject projects=this.projectRepo.findByIsDeletedAndId(false, project_id);
		if(projects!=null) {
			projects.set_deleted(true);
			
	         this.projectRepo.save(projects);
	         return "deleted";
		}

			} catch (Exception e) {
				System.out.println("data not found");
			}
		return "Data Not Found";
	}

	@Override
	public List<EmployeeProjectResponceEntity> getAllEmplyeeProjects() {
		// TODO Auto-generated method stub
		List<EmployeeProject>emppro=this.projectRepo.findbyisdeleteMasters(false);
		//List<PostDto>postDtos=users.stream().map((post)->this.mpodelmapper.map(post,PostDto.class)).collect(Collectors.toList());
	//	List<EmployeeProject> emppro = this.projectRepo.findAll();

	    List<EmployeeProjectResponceEntity> empProj = emppro.stream()
	            .map(proj -> {
	                EmployeeProjectResponceEntity entity = this.mapper.map(proj, EmployeeProjectResponceEntity.class);
	                // Fetch user details by ID and set the name
	                User user = this.userService.findUserByIdUser(proj.getModified_by());
	                System.out.println(proj.getAssign_by());
	                //User user1 = this.userService.findUserByIdUser(proj.getAssign_by());
	                if (user != null) {
	                	//System.out.println(user1.getFull_name()+user.getFull_name());
	                	//entity.setAssign_by(user1.getFull_name());
	                    entity.setModified_by(user.getFull_name());
	                }

	                return entity;
	            })
	            .collect(Collectors.toList());
		//List<EmployeeProjectResponceEntity>empProj=emppro.stream().map((proj)->this.mapper.map(proj,EmployeeProjectResponceEntity.class )).collect(Collectors.toList());
		return empProj;
	}

	@Override
	public EmployeeProjectResponceEntity getbyid(Long id) {
		// TODO Auto-generated method stub
		EmployeeProject emppro=this.projectRepo.findByIsDeletedAndId(false, id);
		EmployeeProjectResponceEntity emppr=mapper.map(emppro, EmployeeProjectResponceEntity.class);
		 User user = this.userService.findUserByIdUser(emppro.getModified_by());
		 emppr.setModified_by(user.getFull_name());
		return emppr;
	}

	
}

