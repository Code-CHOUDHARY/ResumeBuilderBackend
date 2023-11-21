package com.resumebuilder.DTO;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeProjectRequestEntity {
	
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
	
	@CurrentTimestamp()
	private Date modified_on;
	
	private boolean is_deleted;
	
	private Long modified_by;
	
}
