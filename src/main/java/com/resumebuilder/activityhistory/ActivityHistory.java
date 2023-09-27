package com.resumebuilder.activityhistory;

import java.util.Date;

import com.resumebuilder.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



@Entity
@Table(name="ActivityHistory")
public class ActivityHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long activity_id;
	private String activity_name;
	private String activity_type;
	private String activity_for;
	private String old_data;
	private String new_data;
	private String activity_by;
	private Date activity_on;
	
	@ManyToOne
    private User user; // Reference to the User entity

}
