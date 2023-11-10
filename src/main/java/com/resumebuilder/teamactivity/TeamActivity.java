package com.resumebuilder.teamactivity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.resumebuilder.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TeamActivity")
public class TeamActivity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private Long team_acivity_id;
	
	private String employee_name;
	
	@Column(length = 20)
	private Long employee_id;
	private String current_role;
	
	@CreationTimestamp
	private Date activity_on;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column(columnDefinition = "TEXT")
	private String old_data;
	
	@Column(columnDefinition = "TEXT")
	private String new_data;
	

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	private User activity_by; // Reference to the User entity

	
}
