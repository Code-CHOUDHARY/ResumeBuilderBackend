package com.resumebuilder.projects;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.ProjectDataNullException;
import com.resumebuilder.exception.RoleException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class ProjectMasterServiceImplementation implements ProjectMasterService{

//	@Autowired
//	private ProjectMasterRepository projectMasterRepository;
//	
//	@Autowired
//	private UserRepository userRepository;
//	
//	/**
//     * Add a new project.
//     *
//     * @param The project to be added.
//     * @principal represent user identity.
//     * @return Save the project.
//     */
//	
//	@Override
//	public ProjectMaster addProject(ProjectMaster projectMaster, Principal principal) {
//		User user = userRepository.findByEmail_Id(principal.getName());
//		ProjectMaster pm = new ProjectMaster();
//		pm.setProject_title(projectMaster.getProject_title());
//		pm.setProject_summary(projectMaster.getProject_summary());
//		pm.setStart_date(projectMaster.getStart_date());
//		pm.setEnd_date(projectMaster.getEnd_date());
//		pm.setOrganization_name(projectMaster.getOrganization_name());
//		pm.setClient_name(projectMaster.getClient_name());
//		pm.setModified_by(user.getFull_name());
//		pm.setModified_on(projectMaster.getModified_on());
//		pm.setCurrent(false);
//		pm.setProject_url(projectMaster.getProject_url());
//		pm.setRoles_and_responsibility(projectMaster.getRoles_and_responsibility());
//		pm.setShow_dates(false);
//		pm.setShow_duration(projectMaster.getShow_duration());
//		pm.setShow_nothing(false);
//		pm.setTechnology_stack(projectMaster.getTechnology_stack());
//		
//		// Check for null values or missing data in any field
//        if (hasMissingData(projectMaster)) {
//            throw new ProjectDataNullException("Project data is incomplete or contains null values");
//        }
//
//		return projectMasterRepository.save(projectMaster);
//	}
//	
//	// Define a method to check for null values or missing data in any field
//    private boolean hasMissingData(ProjectMaster projectMaster) {
//        return projectMaster.getProject_title() == null ||
//               projectMaster.getProject_summary() == null ||
//               projectMaster.getStart_date() == null ||
//               projectMaster.getEnd_date() == null ||
//               projectMaster.getOrganization_name() == null ||
//               // Add checks for other fields as needed
//               false; // Return false if all fields are non-null (no missing data)
//    }

}
