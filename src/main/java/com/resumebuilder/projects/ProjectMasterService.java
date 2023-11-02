package com.resumebuilder.projects;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.resumebuilder.DTO.ProjectDto;
import com.resumebuilder.DTO.ProjectMasterDto;

//service interface for required method according to functionality.
public interface ProjectMasterService {

	public ProjectMaster addProject(ProjectMaster projectMaster, Principal principal);
	public ProjectMaster updateproject(ProjectMasterResponce projectmaster ,Long projectId, Principal principal);
	public String deleteproject(Long id);
	public List<ProjectMaster> getProjectdata();
	public void deleteAssignProjectByEmployee(Long emp_project_id, Principal principal);

	public ProjectMaster saveOrUpdateEmployeeProject(ProjectDto project, Principal principal);
	// public void assignProjectToUser(Long userId, Long projectId, Principal
	// principal);
	// public EmployeeProject editAssignedProject(Long userId, Long emp_project_id,
	// EmployeeProject updatedProject,Principal principal) throws Exception;

	// ResponseEntity<?> addProject(ProjectMasterDto projectMasterDto, Principal
	// principal);

}
