package com.resumebuilder.projects;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;

import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_project")
public class EmployeeProject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_project_id")
	private Long emp_project_id;
	@Column(name = "project_title")
	private String project_title;
	@Column(name = "start_date")
	private Date start_date;
	@Column(name = "end_date")
	private Date end_date;
	@Column(name = "current")
	private boolean current;
	private boolean show_dates;
	private String show_duration;
	private boolean show_nothing;
	private String project_url;
	private String client_name;
	private String organization_name;
	private String project_summary;
	private String technologies;
	private String technology_stack;
	private String roles_and_responsibility;
	@Column(name = "assign_by")
	private Long assign_by;
	@UpdateTimestamp
	@Column(name = "modified_on")
	private LocalDateTime modified_on;
	private boolean is_deleted;
	private Long modified_by;

	@ManyToMany(mappedBy = "assignedProjects")
	private Set<User> users = new HashSet<>();

//		@ManyToOne
//	    @JoinColumn(name = "user_id")
//	    private User user;

}
