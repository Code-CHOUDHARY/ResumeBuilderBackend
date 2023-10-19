package com.resumebuilder.projects;

import java.security.Principal;

//service interface for required method according to functionality.
public interface ProjectMasterService {
	
	public ProjectMaster addProject(ProjectMaster projectMaster, Principal principal);

}
