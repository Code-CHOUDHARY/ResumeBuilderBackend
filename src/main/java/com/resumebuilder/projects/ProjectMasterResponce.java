package com.resumebuilder.projects;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMasterResponce {
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
	private String technology_stack;
	private String roles_and_responsibility;
	private Long modified_by;
	private boolean is_deleted;
		
	
	
}
