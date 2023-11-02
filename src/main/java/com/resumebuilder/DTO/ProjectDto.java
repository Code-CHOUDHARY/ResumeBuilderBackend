package com.resumebuilder.DTO;

import java.sql.Date;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ProjectDto {

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
	private Long modified_by;
	@UpdateTimestamp
	@Column(name = "modified_on")
	private LocalDateTime modified_on;
	private boolean is_deleted;
	private Long employee_Id;
}
