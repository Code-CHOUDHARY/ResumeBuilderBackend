package com.resumebuilder.education;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Education")
public class Education {
	
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
	@Column(name = "modified_on")
	private LocalDateTime modified_on;
	
	@ManyToOne
	@JoinColumn(name = "employee_Id")
	private User user;

}
