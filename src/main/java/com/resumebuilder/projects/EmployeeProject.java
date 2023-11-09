package com.resumebuilder.projects;


import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.resumebuilder.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "employee_project")
public class EmployeeProject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long emp_project_id;
	private String project_title;
	private Date start_date;
	private Date end_date;
	private boolean current;
	private boolean show_dates;
	private boolean show_duration;
	private boolean show_nothing;
	private String project_url;
	private String client_name;
	private String organization_name;
	private String project_summary;
	private String technologies;
	private String technology_stack;
	private String roles_and_responsibility;
	private Long assign_by;
	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_on")
	private LocalDateTime modified_on;
	private boolean is_deleted;
	private Long modified_by;

//	@ManyToMany(mappedBy = "assignedProjects")
//	private Set<User> users = new HashSet<>();
	@JsonIgnore
		@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	   // @JsonBackReference
	    private User user;

}
