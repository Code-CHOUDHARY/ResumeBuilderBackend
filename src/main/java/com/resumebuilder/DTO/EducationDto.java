package com.resumebuilder.DTO;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class EducationDto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long education_id;
	private String school_college;
	private String degree;
	private Date start_date;
	private Date end_date;
	private boolean show_dates;
	private String show_duration;
	private boolean show_nothing;
	private boolean is_deleted;
	private Long Modified_by;
	@UpdateTimestamp
	private LocalDateTime modified_on;

}
