package com.resumebuilder.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.UpdateTimestamp;

import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.education.Education;
import com.resumebuilder.professionalexperience.ProfessionalExperience;
import com.resumebuilder.projects.EmployeeProject;
import com.resumebuilder.projects.ProjectMaster;
import com.resumebuilder.reportingmanager.ReportingManager;
import com.resumebuilder.roles.Roles;
//import com.resumebuilder.security.approle.AppRole;
import com.resumebuilder.security.approle.UserRole;
import com.resumebuilder.technology.TechnologyMaster;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;
	@Column
	private String full_name;
	@Column(name = "email_id")
	private String email;
	@Column
	private String password;
	@Column
	private String employee_Id;
	@Column
	private String current_role;
	@Column
	private String user_image;
	@Column
	private String gender;
	@Column
	private String mobile_number;
	@Column
	private String location;
	@Column
	private String date_of_joining;
	@Column
	private String date_of_birth;
	@Column
	private String linkedin_lnk;
	@Column
	private String portfolio_link;
	@Column
	private String blogs_link;
	@Lob
    @Column(length = 1000) 
    private String professional_summary;
	@UpdateTimestamp
	private LocalDateTime modified_on;
	@Column
	private String modified_by;
	@Column
	private boolean is_deleted;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles",
//	        joinColumns = @JoinColumn(name = "user_id"), 
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Roles> roles = new HashSet<>();

	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "manager_project_mapping",
            joinColumns = {@JoinColumn(name = "manager_employee_Id")},
            inverseJoinColumns = {@JoinColumn(name = "project_master_id")})
    private Set<ProjectMaster> projects = new HashSet<>();
	


//	@ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "manager_project_mapping",
//            joinColumns = {@JoinColumn(name = "manager_employee_Id")},
//            inverseJoinColumns = {@JoinColumn(name = "project_master_id")})
//    private Set<ProjectMaster> projects = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "user_project", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<EmployeeProject> assignedProjects = new HashSet<>();
	

//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//	private List<EmployeeProject> employeeProject = new ArrayList<>();



	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "employee_technology_mapping", joinColumns = @JoinColumn(name = "employee_Id"), inverseJoinColumns = @JoinColumn(name = "technology_id"))
	private Set<TechnologyMaster> technologies = new HashSet<>();

	@OneToMany(mappedBy = "user") // A user can be associated with many activities
	private List<ActivityHistory> activityHistories; // Reference to the ActivityHistory entity
	
	
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	private UserRole appRole;

}
