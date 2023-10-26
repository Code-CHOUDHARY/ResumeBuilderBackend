package com.resumebuilder.projects;

import java.security.Principal;

//service interface for required method according to functionality.
public interface ProjectMasterService {
	
	public ProjectMaster addProject(ProjectMaster projectMaster, Principal principal);
	public void deleteProjectMasterAndAssignProject(Long projectId, Long emp_project_id);
	public void assignProjectToUser(Long userId, Long projectId, Principal principal);
	public EmployeeProject editAssignedProject(Long userId, Long emp_project_id, EmployeeProject updatedProject,Principal principal) throws Exception;
}
