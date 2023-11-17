package com.resumebuilder.DTO;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDto {
	private Long template_id;
private String template_name;
	private String profile_summary;

	private String professional_experience;
	
	

	private String projects;
	
	

	private String certificates;
	
	
	private String modified_by;

	
	private Date modified_on;

  
}
