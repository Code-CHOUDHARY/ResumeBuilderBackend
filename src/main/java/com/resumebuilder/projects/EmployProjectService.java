package com.resumebuilder.projects;

import java.security.Principal;
import java.util.List;

import com.resumebuilder.DTO.EmployeProjectRequestEntity;
import com.resumebuilder.DTO.EmployeeProjectResponceEntity;

public interface EmployProjectService {

	
	EmployeeProject addEmployeeProject(EmployeProjectRequestEntity employeeProjectData,Long id,Principal princi);
	     
	EmployeeProject updateEmployeeProject(EmployeProjectRequestEntity projects,Long project_id,Principal principal);  
	
	String deleteEmployeeProject(Long project_id);
	
	List<EmployeeProjectResponceEntity> getAllEmplyeeProjects();
	
	EmployeeProjectResponceEntity getbyid(Long id);
 }
