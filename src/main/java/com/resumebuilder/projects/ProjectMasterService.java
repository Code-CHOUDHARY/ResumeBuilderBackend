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
	public ProjectMaster saveOrUpdateEmployeeProject(ProjectDto project, Principal principal);
	public void deleteAssignProjectByEmployee(Long emp_project_id, Principal principal);
	ProjectMaster getprojectById(Long id);
}
