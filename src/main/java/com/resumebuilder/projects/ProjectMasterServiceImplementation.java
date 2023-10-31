package com.resumebuilder.projects;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.resumebuilder.DTO.ProjectMasterDto;
import com.resumebuilder.exception.DataMissingException;
import com.resumebuilder.exception.ProjectException;
import com.resumebuilder.exception.ProjectNotFoundException;
import com.resumebuilder.exception.RoleException;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.projects.ProjectMaster.ProjectMasterBuilder;
import com.resumebuilder.reportingmanager.ReportingManager;
import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.approle.UserRole;
import com.resumebuilder.security.response.MessageResponse;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class ProjectMasterServiceImplementation implements ProjectMasterService{

	@Autowired
	private ProjectMasterRepository projectMasterRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	@Autowired
	private EmployeeProjectRepository employeeProjectRepository;
	
	/**
     * Add a new project.
     *
     * @param The project to be added.
     * @principal represent user identity.
     * @return Save the project.
	 * @throws Exception 
     */
	
	@Override
	public ProjectMaster addProject(ProjectMaster projects, Principal principal) {
		try {
		
			User user = userRepository.findByEmail_Id(principal.getName());
			ProjectMaster projectMaster = ProjectMaster.builder()
					.project_title(projects.getProject_title())
					.project_master_id(projects.getProject_master_id())
					   .start_date(projects.getStart_date())
				          .end_date(projects.getEnd_date())
				          .current(projects.isCurrent())
				          .show_dates(projects.isShow_dates())
				          .show_duration(projects.getShow_duration())
				          .show_nothing(projects.isShow_nothing())
				          .project_url(projects.getProject_url())
				          .client_name(projects.getProject_url())
				          .organization_name(projects.getOrganization_name())
				          .project_summary(projects.getProject_summary())
				          .technology_stack(projects.getTechnology_stack())
				          .roles_and_responsibility(projects.getRoles_and_responsibility())
				          .modified_by(user.getUser_id()).build();
		    
			return projectMasterRepository.save(projectMaster);
			
		} catch (Exception e) {
			throw new RuntimeException("Error adding project", e);
		}
		
	}

	@Override
	public ProjectMaster updateproject(ProjectMasterResponce projects, Long projectId, Principal principal) {
		// TODO Auto-generated method stub
		try {
		User user = userRepository.findByEmail_Id(principal.getName());
		ProjectMaster project=this.projectMasterRepository.findByIsDeletedAndId(false, projectId);
		
		project.setProject_title(projects.getProject_title());
		project.setStart_date(projects.getStart_date());
		project.setEnd_date(projects.getEnd_date());
		project.setCurrent(projects.isCurrent());
		project.setShow_dates(projects.isShow_dates());
		project.setShow_duration(projects.getShow_duration());
		project.setShow_nothing(projects.isShow_nothing());
		project.setProject_url(projects.getProject_url());
		project.setClient_name(projects.getProject_url());
		project.setOrganization_name(projects.getOrganization_name());
		project.setProject_summary(projects.getProject_summary());
		project.setTechnology_stack(projects.getTechnology_stack());
		project.setRoles_and_responsibility(projects.getRoles_and_responsibility());
		project.setModified_by(user.getUser_id());
		return this.projectMasterRepository.save(project);
	} catch (Exception e) {
		
		System.out.println("data not found");
		return null;
	}
	
		
	}

	@Override
	public String deleteproject(Long id) {
		try {
			ProjectMaster project=this.projectMasterRepository.findByIsDeletedAndId(false, id);
		System.out.println("Project data=="+project);
if(project!=null) {
         project.set_deleted(true);

         this.projectMasterRepository.save(project);
         return "deleted";
	}

		} catch (Exception e) {
			System.out.println("data not found");
		}
return "Data Not Found";
	
   
	}

	@Override
	public List<ProjectMaster> getProjectdata( ) {
		// TODO Auto-generated method stub
		try {
			List<ProjectMaster> project=this.projectMasterRepository.findbyisdeleteMasters(false);
		System.out.println("Project data=="+project);
if(project!=null) {
         

         
         return project;
	}

		} catch (Exception e) {
			System.out.println("data not found");
		}
		return null;
	}

	
	
}
