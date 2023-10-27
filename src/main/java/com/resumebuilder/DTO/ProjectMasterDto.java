package com.resumebuilder.DTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
public class ProjectMasterDto {

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
	@UpdateTimestamp	
	private LocalDateTime modified_on;
	private boolean is_deleted;
	private List<Long> employeeIds;
}
