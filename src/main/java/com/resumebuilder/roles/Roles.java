package com.resumebuilder.roles;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.resumebuilder.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
	private String modified_by; // according to token store the name of current user in database
	@CreationTimestamp
	private LocalDateTime modified_on;
	private boolean is_deleted;
	


}
