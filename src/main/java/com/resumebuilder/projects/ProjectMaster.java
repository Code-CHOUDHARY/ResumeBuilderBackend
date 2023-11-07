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
import lombok.*;

//ProjectMaster Entity class with all required fields.

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Project_master")
public class ProjectMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long project_master_id;
	private String project_title;
	private Date start_date;
	private Date end_date;
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
	private String modified_by;
	private String assign_by;
	@UpdateTimestamp
	@Column(name = "modified_on")
	private LocalDateTime modified_on;
	private boolean is_deleted;

	@ManyToMany(mappedBy = "projects")
	private Set<User> users = new HashSet<>();

//	@ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

}
