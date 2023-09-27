package com.resumebuilder.resumetemplates;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ResumeTemplate")
public class ResumeTemplates {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long template_id;
	private String template_name;
	private String profile_summary;
	private String professional_experience;
	private String projects;
	private String certificates;
	private String modified_by;
	private Date modified_on;
	private boolean is_deleted;
	
//	@ManyToOne
//    @JoinColumn(name = "modified_by", referencedColumnName = "full_name", insertable = false, updatable = false)
//    private User user; // Reference to the User entity

}
