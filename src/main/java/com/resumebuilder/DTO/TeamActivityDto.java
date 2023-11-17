package com.resumebuilder.DTO;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.resumebuilder.resumes.ResumeResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamActivityDto {
		
	private String employee_name;
	private String employee_id;
	private String current_role;
	private String activty_by;	
	private Date activity_on;
	private String description;
	private String old_data;
	private String new_data;
	
}
