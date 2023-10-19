package com.resumebuilder.activityhistory;

import java.math.BigInteger;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;

import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="ActivityHistory")
public class ActivityHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long activity_id;	
	private String activity_type;
	private String description;
	@Column(columnDefinition = "TEXT")
	private String old_data;
	@Column(columnDefinition = "TEXT")
	private String new_data;
	@CreatedBy
	private String activity_by;
	@CreationTimestamp
	private Date activity_on;
	
	 	@ManyToOne // Many activities can be associated with one user
	    @JoinColumn(name = "user_id") // Name of the foreign key column in the ActivityHistory table
	    private User user; // Reference to the User entity

}
