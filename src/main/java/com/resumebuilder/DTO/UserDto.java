package com.resumebuilder.DTO;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;

import com.resumebuilder.projects.EmployeeProject;
import com.resumebuilder.projects.ProjectMaster;
import com.resumebuilder.security.approle.UserRole;
import com.resumebuilder.technology.TechnologyMaster;
import com.resumebuilder.technologyExpertise.TechnologyExpertise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	
	private long user_id;
    private String employee_Id;
    private String full_name;
    private String date_of_joining;
    private String date_of_birth;
    private String current_role;
    private String email;
    private String gender;
    private String mobile_number;
    private String location;
    private String manager_employee_id;
    private String image;
    @UpdateTimestamp
	private LocalDateTime modified_on;
    private String user_image;
	private String linkedin_lnk;
	private String portfolio_link;
	private String blogs_link;
    private String professional_summary;
    private String technology_stack;
	private String modified_by;
	private List<Long> managerIds;
	private Set<ProjectMaster> projects = new HashSet<>();
	private List<EmployeeProject> employeeProject = new ArrayList<>();
	private Set<TechnologyMaster> technologies = new HashSet<>();
	 private TechnologyExpertise technologyExpertise;
	 private UserRole appRole;
}
