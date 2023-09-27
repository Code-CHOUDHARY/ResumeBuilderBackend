package com.resumebuilder.teamactivity;

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
@Table(name="TeamActivity")
public class TeamActivity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long team_acivity_id;
	private String employee_name;
	private String employee_id;
	private String current_role;
	private String activity_by;
	private Date activity_on;
	private String description;
	private String old_data;
	private String new_data;
	
//	@ManyToOne
//	@JoinColumn(name = "employee_id", referencedColumnName = "employee_Id", insertable = false, updatable = false)
//	private User user; // Reference to the User entity

}
