package com.resumebuilder.DTO;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.resumebuilder.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProjectResponceEntity {
	
	private String project_title;
	private Date startDate;
	private Date endDate;
	private boolean current;
	private boolean showDates;
	
	private boolean show_duration;
	
	private boolean show_nothing;
	
	private String project_url;
	
	private String client_name;
	
	private String organization_name;
	
	private String project_summary;
	
	private String technology_stack;
	
	private String roles_and_responsibility;
	
	private String assign_by;
	
	@UpdateTimestamp
	private LocalDateTime modified_on;
	
	private String modified_by;
//	 private User user;

}
