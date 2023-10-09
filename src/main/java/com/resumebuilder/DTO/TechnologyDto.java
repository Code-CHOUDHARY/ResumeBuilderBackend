package com.resumebuilder.DTO;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
public class TechnologyDto {
	
	@UpdateTimestamp
	private LocalDateTime modified_on;
	private String technology_name;

}
