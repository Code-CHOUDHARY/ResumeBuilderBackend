package com.resumebuilder.projects;

public interface EmployProjectService {

	
	EmployeeProject addEmployeeProject(EmployeProjectResponceEntity employeeProjectData);
	     
	EmployeeProject updateEmployeeProject(EmployeProjectResponceEntity projects,Long project_id);  
	
	void deleteEmployeeProject(Long project_id);
	
	EmployeeProject getAllEmplyeeProjects();
	
 }
