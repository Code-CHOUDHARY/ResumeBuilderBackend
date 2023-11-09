package com.resumebuilder.resumetemplates;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
	
	
	@Column(columnDefinition = "TEXT")
	private String profile_summary;
	
	
	@Column(columnDefinition = "TEXT")
	private String professional_experience;
	
	
	@Column(columnDefinition = "TEXT")
	private String projects;
	
	
	@Column(columnDefinition = "TEXT")
	private String certificates;
	
	private String modified_by;

	
	private Date modified_on;
	
    @Column(columnDefinition = "boolean")
    @JsonIgnore  //not sending in response
	private boolean is_deleted;
	
//	@JsonIgnore
	//@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
//    private User Modified_by; // Reference to the User entity

}
