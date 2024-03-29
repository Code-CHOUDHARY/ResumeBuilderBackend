package com.resumebuilder.roles;

import java.time.LocalDateTime;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

//Roles Entity class with all required fields.

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Roles")
public class Roles {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long role_id;
	private String role_name;
	private Long modified_by; // according to token store the name of current user in database
	@UpdateTimestamp
	@Column(name = "modified_on")
	private LocalDateTime modified_on;
	private boolean is_deleted;

}
