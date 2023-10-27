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
import com.resumebuilder.exception.ProjectException;
import com.resumebuilder.exception.ProjectNotFoundException;
import com.resumebuilder.exception.RoleException;
import com.resumebuilder.exception.UserNotFoundException;
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
	private ProjectAssignRepository projectAssignRepository;
	
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
	public ProjectMaster addProject(ProjectMaster projectMaster, Principal principal) {
		try {
			User user = userRepository.findByEmail_Id(principal.getName());
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
	
//	@Override
//	public ResponseEntity<?> addProject(ProjectMasterDto projectMasterDto, Principal principal) {
//	    try {
//	        User user = userRepository.findByEmail_Id(principal.getName());
//
//	        ProjectMaster existingProject = projectMasterRepository.findByProjectTitle(projectMasterDto.getProject_title());
//	        ProjectMaster projectMaster;
//	        
//	        if (existingProject != null) {
//	            // A project with the same title already exists, so create a new one
//	            ProjectMaster newProject = new ProjectMaster();
//	            newProject.setProject_title(projectMasterDto.getProject_title());
//	            newProject.setProject_summary(projectMasterDto.getProject_summary());
//	            newProject.setStart_date(projectMasterDto.getStart_date());
//	            newProject.setEnd_date(projectMasterDto.getEnd_date());
//	            newProject.setOrganization_name(projectMasterDto.getOrganization_name());
//	            newProject.setClient_name(projectMasterDto.getClient_name());
//	            newProject.setModified_by(user.getUser_id());
//	            newProject.setModified_on(projectMasterDto.getModified_on());
//	            newProject.setCurrent(false);
//	            newProject.setProject_url(projectMasterDto.getProject_url());
//	            newProject.setRoles_and_responsibility(projectMasterDto.getRoles_and_responsibility());
//	            newProject.setShow_dates(false);
//	            newProject.setShow_duration(projectMasterDto.getShow_duration());
//	            newProject.setShow_nothing(false);
//	            newProject.setTechnology_stack(projectMasterDto.getTechnology_stack());
//	            newProject.set_deleted(false);
//
//	           projectMaster = projectMasterRepository.save(newProject);
//	        } else {
//	            // Create a new project
//	            ProjectMaster newProject = new ProjectMaster();
//	            newProject.setProject_title(projectMasterDto.getProject_title());
//	            newProject.setProject_summary(projectMasterDto.getProject_summary());
//	            newProject.setStart_date(projectMasterDto.getStart_date());
//	            newProject.setEnd_date(projectMasterDto.getEnd_date());
//	            newProject.setOrganization_name(projectMasterDto.getOrganization_name());
//	            newProject.setClient_name(projectMasterDto.getClient_name());
//	            newProject.setModified_by(user.getUser_id());
//	            newProject.setModified_on(projectMasterDto.getModified_on());
//	            newProject.setCurrent(false);
//	            newProject.setProject_url(projectMasterDto.getProject_url());
//	            newProject.setRoles_and_responsibility(projectMasterDto.getRoles_and_responsibility());
//	            newProject.setShow_dates(false);
//	            newProject.setShow_duration(projectMasterDto.getShow_duration());
//	            newProject.setShow_nothing(false);
//	            newProject.setTechnology_stack(projectMasterDto.getTechnology_stack());
//	            newProject.set_deleted(false);
//
//	             projectMaster = projectMasterRepository.save(newProject);
//	        }
//
//	        for (Long employeeId : projectMasterDto.getEmployeeIds()) {
//	            User employee = userRepository.findById(employeeId).orElseThrow(() -> new Exception("Employee not found with ID: " + employeeId));
//	            ProjectAssign assign = new ProjectAssign();
//	            assign.setEmployee(employee);
//	            assign.setProject(projectMaster);
//	            projectAssignRepository.save(assign);
//	        }
//
//	        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Project data added successfully."));
//	    } catch (Exception e) {
//	        throw new ProjectNotFoundException("Failed to add project data. " + e.getMessage());
//	    }
//	}

	
	
	/**
     * Assign a project to a user.
     *
     * @param userId     The ID of the user to whom the project will be assigned.
     * @param projectId  The ID of the project to be assigned.
     * @param principal  Represents user identity.
     */

	@Override
	public void assignProjectToUser(Long userId, Long projectId, Principal principal) {
		//User user = userRepository.findById(userId).orElse(null);
		
		User currentUser = userRepository.findByEmail_Id(principal.getName());
        ProjectMaster projectMaster = projectMasterRepository.findById(projectId).orElse(null);
       
        if(projectMaster == null) {
        	throw new IllegalArgumentException("Project not found with ID: " + projectId);
        }
        
        if (projectMaster.is_deleted()) {
            throw new ProjectException("Project does not exist.");
        }
        
        EmployeeProject employeeProject = new EmployeeProject();
        employeeProject.setProject_summary(projectMaster.getProject_summary());
        employeeProject.setProject_title(projectMaster.getProject_title());
        employeeProject.setOrganization_name(projectMaster.getOrganization_name());
        employeeProject.setProject_url(projectMaster.getProject_url());
        employeeProject.setTechnology_stack(projectMaster.getTechnology_stack());
        employeeProject.setAssign_by(currentUser.getUser_id());
        employeeProject.setModified_on(LocalDateTime.now());
        
        User user = userRepository.findById(userId).orElse(null);
        
        if(user==null) {
        	throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        user.getAssignedProjects().add(employeeProject);
        userRepository.save(user);
		
	}
	
	/**
     * Edit an assigned project.
     *
     * @param userId        The ID of the user to whom the project is assigned.
     * @param emp_project_id The ID of the assigned project.
     * @param updatedProject The updated project details.
     * @param principal      Represents user identity.
     * @return The edited project.
     * @throws Exception If an error occurs during editing.
     */
	
	
	@Override
	public EmployeeProject editAssignedProject(Long userId, Long emp_project_id, EmployeeProject updatedProject, Principal principal) throws Exception {
	   try {
		   User currentUser = userRepository.findByEmail_Id(principal.getName());   // Get the current user
	   
	    User user = userRepository.findById(userId).orElse(null);  // Get the user to whom the project is assigned

	    if (user == null) {
	        throw new IllegalArgumentException("User not found with ID: " + userId);
	    }
	    
	    // Find the assigned project by its ID
	    Optional<EmployeeProject> optionalExistingProject = employeeProjectRepository.findById(emp_project_id);

	    if (optionalExistingProject.isPresent()) {
	        EmployeeProject existingProject = optionalExistingProject.get();
	        
	        if (existingProject.is_deleted()) {
	        	throw new ProjectException("Assigned project is not exist with ID: " + emp_project_id);
	        }

	        // Update the project details with the provided updatedProject
	        existingProject.setProject_summary(updatedProject.getProject_summary());
	        existingProject.setProject_title(updatedProject.getProject_title());
	        existingProject.setOrganization_name(updatedProject.getOrganization_name());
	        existingProject.setProject_url(updatedProject.getProject_url());
	        existingProject.setTechnology_stack(updatedProject.getTechnology_stack());
	        existingProject.setModified_by(currentUser.getUser_id());

	        return employeeProjectRepository.save(existingProject);
	    }
	   }catch(Exception e) {
		   throw new ProjectException("Project does not exist.");
	    }
	return updatedProject;
	}


//	@Override
//	public EmployeeProject editAssignedProject(Long userId, Long emp_project_id, EmployeeProject updatedProject, Principal principal) throws Exception {
//	    
//		User currentUser = userRepository.findByEmail_Id(principal.getName());   // Get the current user
//		User user = userRepository.findById(userId).orElse(null);  // Get the user to whom the project is assigned
//
//	    if (user == null) {
//	        throw new IllegalArgumentException("User not found with ID: " + userId);
//	    }
//
//	    // Find the assigned project by its ID
////	    EmployeeProject existingProject = user.getAssignedProjects().stream()
////	            .filter(project -> project.getEmp_project_id().equals(emp_project_id))
////	            .findFirst()
////	            .orElse(null);
//
////	    if (existingProject == null) {
////	        throw new IllegalArgumentException("Assigned project not found with ID: " + emp_project_id);
////	    }
//	    
//	 // Find the assigned project by its ID
//	    EmployeeProject existingProject = employeeProjectRepository.findById(emp_project_id)
//	    		.orElseThrow(()-> new Exception("Assigned project not found with ID: " + emp_project_id));
//
//	    // Update the project details with the provided updatedProject
//	    existingProject.setProject_summary(updatedProject.getProject_summary());
//	    existingProject.setProject_title(updatedProject.getProject_title());
//	    existingProject.setOrganization_name(updatedProject.getOrganization_name());
//	    existingProject.setProject_url(updatedProject.getProject_url());
//	    existingProject.setTechnology_stack(updatedProject.getTechnology_stack());
//	    existingProject.setModified_by(currentUser.getFull_name());
//
//	    return employeeProjectRepository.save(existingProject);
//	}
	
	
	
	
	@Override
	public void deleteProjectMasterAndAssignProject(Long projectId, Long emp_project_id, Principal principal) {
		User currentUser = userRepository.findByEmail_Id(principal.getName());
		ProjectMaster project = projectMasterRepository.findById(projectId).orElse(null);
	    if (project != null) {
	        project.set_deleted(true);
	        project.setModified_by(currentUser.getUser_id());
	        projectMasterRepository.save(project);
	    }

	    EmployeeProject assignProject = employeeProjectRepository.findById(emp_project_id).orElse(null);
	    if (assignProject != null) {
	    	assignProject.set_deleted(true);
	    	assignProject.setModified_by(currentUser.getUser_id());
	        employeeProjectRepository.save(assignProject);
	    }
	}
	
	
    public List<EmployeeProject> getAssignedProjectsByUserId(Long userId) {
        return employeeProjectRepository.findByUsersUserId(userId);
    }
	

}
