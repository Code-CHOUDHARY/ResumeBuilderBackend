package com.resumebuilder.DTO;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityHistoryDto {
	
	private String activity_type;
	private String description;
	@Column(columnDefinition = "TEXT")
	private String old_data;
	@Column(columnDefinition = "TEXT")
	private String new_data;

	private Long activity_for;
	
	private String activity_by;
	@CreationTimestamp
	private Date activity_on; 
}
