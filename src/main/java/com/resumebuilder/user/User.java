package com.resumebuilder.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.UpdateTimestamp;
import com.resumebuilder.education.Education;
import com.resumebuilder.projects.ProjectMaster;
import com.resumebuilder.technology.TechnologyMaster;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name="User")
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
	private String application_role;//(Admin, Manager, Employee)
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
	@UpdateTimestamp
	private LocalDateTime modified_on;
	@Column
	private String modified_by;
	@Column
	private boolean is_deleted;
	
//	private boolean status;
//	private String remark;
	
	@ManyToMany
    @JoinTable(name = "user_project_mapping",
            joinColumns = {@JoinColumn(name = "employee_Id")},
            inverseJoinColumns = {@JoinColumn(name = "project_master_id")})
    private Set<ProjectMaster> projects = new HashSet<>();
	
	
	@OneToMany(mappedBy = "user")
	private List<Education> educations = new ArrayList<>();

	
	@ManyToMany
	@JoinTable(name = "employee_technology_mapping",
	    joinColumns = @JoinColumn(name = "employee_Id"),
	    inverseJoinColumns = @JoinColumn(name = "technology_id")
	)
	private Set<TechnologyMaster> technologies = new HashSet<>();


}
