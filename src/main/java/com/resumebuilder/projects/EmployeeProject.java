package com.resumebuilder.projects;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;

import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name="employee_project")
public class EmployeeProject {

		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "emp_project_id")
		private Long projectId;
		
		@Column(name = "project_title")
		private String projectTitle;
		
		@Column(name = "start_date")
		private Date startDate;
		
		@Column(name = "end_date")
		private Date endDate;
		
		@Column(name = "current")
		private boolean current;
		
		private boolean showDates;
		
		private String show_duration;
		
		private boolean show_nothing;
		
		private String project_url;
		
		private String client_name;
		
		private String organization_name;
		
		private String project_summary;
		
		private String technology_stack;
		
		private String roles_and_responsibility;
		
		@Column(name = "assign_by")
		private Long assign_by;
		
		@UpdateTimestamp
		@Column(name = "modified_on")
		private LocalDateTime modified_on;
		
		private boolean is_deleted;
		
		private Long modified_by;	
		@Builder.Default
		  @ManyToMany(mappedBy = "assignedProjects")
	    private Set<User> users = new HashSet<>();


}
