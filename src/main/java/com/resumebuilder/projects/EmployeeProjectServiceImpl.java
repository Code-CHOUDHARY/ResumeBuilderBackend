package com.resumebuilder.projects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.resumebuilder.exception.DataMissingException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

public class EmployeeProjectServiceImpl implements EmployProjectService{
       @Autowired
	private UserRepository userRepo;
	@Autowired
	private EmployeeProjectRepository projectRepo;
	@Override
	public EmployeeProject addEmployeeProject(EmployeProjectResponceEntity projects) {
		// TODO Auto-generated method stub
		//List<User>users=this.userRepo.findByIdIn(projects.getUserids());
		List<User>users=this.userRepo.findAllById(projects.userids);
			System.out.println(users);
		EmployeeProject project=	 EmployeeProject.builder().projectTitle(projects.getProjectTitle())
	          .startDate(projects.getStartDate())
	          .endDate(projects.getEndDate())
	          .current(projects.isCurrent())
	          .showDates(projects.isShowDates())
	          .show_duration(projects.getShow_duration())
	          .show_nothing(projects.isShow_nothing())
	          .project_url(projects.getProject_url())
	          .client_name(projects.getProject_url())
	          .organization_name(projects.getOrganization_name())
	          .project_summary(projects.getProject_summary())
	          .technology_stack(projects.getTechnology_stack())
	          .assign_by(projects.getAssign_by())
	          .modified_by(projects.getModified_by())
	       .build();
	          
			 return this.projectRepo.save(project);
	
		}

	@Override
	public EmployeeProject updateEmployeeProject(EmployeProjectResponceEntity projects, Long project_id) {
		// TODO Auto-generated method stub
		//List<User>users=this.userRepo.findByIdIn(projects.getUserids());
			EmployeeProject project=this.projectRepo.findById(project_id).orElseThrow(()->new DataMissingException("Project", "ProjectId", project_id));
			
			project=	EmployeeProject.builder().projectTitle(projects.getProjectTitle())
	          .startDate(projects.getStartDate())
	          .endDate(projects.getEndDate())
	          .current(projects.isCurrent())
	          .showDates(projects.isShowDates())
	          .show_duration(projects.getShow_duration())
	          .show_nothing(projects.isShow_nothing())
	          .project_url(projects.getProject_url())
	          .client_name(projects.getProject_url())
	          .organization_name(projects.getOrganization_name())
	          .project_summary(projects.getProject_summary())
	          .technology_stack(projects.getTechnology_stack())
	          .assign_by(projects.getAssign_by())
	          .modified_by(projects.getModified_by())
	          .build();
          
		return this.projectRepo.save(project);
	}

	@Override
	public void deleteEmployeeProject(Long project_id) {
		EmployeeProject projects=this.projectRepo.findById(project_id).orElseThrow(()->new DataMissingException("Project", "ProjectId", project_id));
      		
		this.projectRepo.delete(projects);
	}

	@Override
	public EmployeeProject getAllEmplyeeProjects() {
		// TODO Auto-generated method stub
		return null;
	}

}
