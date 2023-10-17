package com.resumebuilder.activityhistory;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

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
	
	private String activity_type;

	private String old_data;
	private String new_data;
	private String activity_by;
	@CreationTimestamp
	private Date activity_on;
	

}
