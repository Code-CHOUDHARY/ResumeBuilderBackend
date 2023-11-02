package com.resumebuilder.projects;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.resumebuilder.DTO.ProjectDto;
import com.resumebuilder.exception.ProjectException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectMasterServiceImplementation implements ProjectMasterService {

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
			ProjectMaster pm = new ProjectMaster();
			pm.setProject_title(projectMaster.getProject_title());
			pm.setProject_summary(projectMaster.getProject_summary());
			pm.setStart_date(projectMaster.getStart_date());
			pm.setEnd_date(projectMaster.getEnd_date());
			pm.setOrganization_name(projectMaster.getOrganization_name());
			pm.setClient_name(projectMaster.getClient_name());
			pm.setModified_by(user.getUser_id());
			pm.setModified_on(projectMaster.getModified_on());
			pm.setCurrent(false);
			pm.setProject_url(projectMaster.getProject_url());
			pm.setRoles_and_responsibility(projectMaster.getRoles_and_responsibility());
			pm.setShow_dates(false);
			pm.setShow_duration(projectMaster.getShow_duration());
			pm.setShow_nothing(false);
			pm.setTechnology_stack(projectMaster.getTechnology_stack());

			user.getProjects().add(pm);
			userRepository.save(user);

			return projectMasterRepository.save(projectMaster);

		} catch (Exception e) {
			throw new RuntimeException("Error adding project", e);
		}

	}


	/**
	 * Add or update project for employee.
	 *
	 * @param The project to be added.
	 * @principal represent user identity.
	 * @return Save the project.
	 * @throws Exception
	 */



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

	@Transactional
	public ProjectMaster saveOrUpdateEmployeeProject(ProjectDto projectDTO, Principal principal) {
		// Get the currently logged-in user
		User user = userRepository.findByEmail_Id(principal.getName());

		// Create or update the project
		ProjectMaster project = projectDTOToEntity(projectDTO, principal);
		project.setModified_by(user.getUser_id());
		project.setModified_on(LocalDateTime.now());
		project = projectMasterRepository.save(project);

		// Create the corresponding EmployeeProject and store the relationship
		EmployeeProject employeeProject = new EmployeeProject();
		employeeProject.setProject_title(project.getProject_title());
		employeeProject.setStart_date(project.getStart_date());
		employeeProject.setEnd_date(project.getEnd_date());
		employeeProject.setCurrent(false);
		employeeProject.setShow_dates(false);
		employeeProject.setShow_duration(project.getShow_duration());
		employeeProject.setShow_nothing(false);
		employeeProject.setClient_name(project.getClient_name());
		employeeProject.setOrganization_name(project.getOrganization_name());
		employeeProject.setProject_url(project.getProject_url());
		employeeProject.setProject_summary(project.getProject_summary());
		employeeProject.setTechnology_stack(project.getTechnology_stack());
		employeeProject.setRoles_and_responsibility(project.getRoles_and_responsibility());
		employeeProject.setAssign_by(user.getUser_id());
		employeeProject.setModified_by(user.getUser_id());
		employeeProject.setModified_on(LocalDateTime.now());

		// Save the EmployeeProject
		employeeProject = employeeProjectRepository.save(employeeProject);

		// Update the relationship between the user and EmployeeProject
		user.getAssignedProjects().add(employeeProject);
		userRepository.save(user);
	
		
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
		return project;
		}
====================================AUTO GENERATED BY CONFLICT EXTENSION====================================
	}
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>AUTO GENERATED BY CONFLICT EXTENSION>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> feature/EmployeeProject

<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<AUTO GENERATED BY CONFLICT EXTENSION<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< dev
return "Data Not Found";
====================================AUTO GENERATED BY CONFLICT EXTENSION====================================
	private ProjectMaster projectDTOToEntity(ProjectDto projectDTO, Principal principal) {
		User user = userRepository.findByEmail_Id(principal.getName());
		// Convert the ProjectDTO to a ProjectMaster entity
		ProjectMaster project = new ProjectMaster();
		project.setProject_master_id(projectDTO.getProject_master_id());
		project.setProject_title(projectDTO.getProject_title());
		project.setStart_date(projectDTO.getStart_date());
		project.setEnd_date(projectDTO.getEnd_date());
		project.setCurrent(false);
		project.setShow_dates(false);
		project.setShow_duration(projectDTO.getShow_duration());
		project.setShow_nothing(false);
		project.setClient_name(projectDTO.getClient_name());
		project.setOrganization_name(projectDTO.getOrganization_name());
		project.setProject_url(projectDTO.getProject_url());
		project.setProject_summary(projectDTO.getProject_summary());
		project.setTechnology_stack(projectDTO.getTechnology_stack());
		project.setRoles_and_responsibility(projectDTO.getRoles_and_responsibility());
		// project.setAssign_by(user.getUser_id());
		project.setModified_by(user.getUser_id());
		project.setModified_on(LocalDateTime.now());
		return project;
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>AUTO GENERATED BY CONFLICT EXTENSION>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> feature/EmployeeProject
	
   
	}

	@Override
<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<AUTO GENERATED BY CONFLICT EXTENSION<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< dev
	public List<ProjectMaster> getProjectdata( ) {
		// TODO Auto-generated method stub
		try {
			List<ProjectMaster> project=this.projectMasterRepository.findbyisdeleteMasters(false);
		System.out.println("Project data=="+project);
if(project!=null) {
         

         
         return project;
====================================AUTO GENERATED BY CONFLICT EXTENSION====================================
	public void deleteAssignProjectByEmployee(Long emp_project_id, Principal principal) {
		User currentUser = userRepository.findByEmail_Id(principal.getName());

		EmployeeProject assignProject = employeeProjectRepository.findById(emp_project_id).orElse(null);
		if (assignProject != null) {
			assignProject.set_deleted(true);
			assignProject.setModified_by(currentUser.getUser_id());
			employeeProjectRepository.save(assignProject);
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>AUTO GENERATED BY CONFLICT EXTENSION>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> feature/EmployeeProject
<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<AUTO GENERATED BY CONFLICT EXTENSION<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< dev
	}
====================================AUTO GENERATED BY CONFLICT EXTENSION====================================
		}
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>AUTO GENERATED BY CONFLICT EXTENSION>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> feature/EmployeeProject

		} catch (Exception e) {
			System.out.println("data not found");
		}
<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<AUTO GENERATED BY CONFLICT EXTENSION<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< dev
		return null;
====================================AUTO GENERATED BY CONFLICT EXTENSION====================================

	public List<EmployeeProject> getAssignedProjectsByUserId(Long userId) {
		return employeeProjectRepository.findByUsersUserId(userId);
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>AUTO GENERATED BY CONFLICT EXTENSION>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> feature/EmployeeProject
	}

<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<AUTO GENERATED BY CONFLICT EXTENSION<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< dev
	
====================================AUTO GENERATED BY CONFLICT EXTENSION====================================
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>AUTO GENERATED BY CONFLICT EXTENSION>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> feature/EmployeeProject
	
}
