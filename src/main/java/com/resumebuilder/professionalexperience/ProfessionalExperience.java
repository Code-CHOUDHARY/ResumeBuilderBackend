package com.resumebuilder.professionalexperience;

import java.util.Date;

import com.resumebuilder.user.User;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name="ProfessionalExperience")
public class ProfessionalExperience {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long professional_experience_id;
	private String job_title;
	private String organization_name;
	private String location;
	private String start_date;
	private String end_date;
	private boolean current;
	private boolean show_dates;
	private boolean show_duration;
	private boolean show_nothing;
	private boolean is_deleted;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Reference to the User entity

}
