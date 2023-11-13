package com.resumebuilder.technologyExpertise;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TechnologyExpertise {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "tech_id")
	    private Long techId;

	    @OneToOne
	    @JoinColumn(name = "user_id")
	    private User user;

	    @Column
	    private String proficient_in; // Example: "Java, Python, C, C++, R, Ruby"

	    @Column
	    private String familiar_with;
	    
	    @Column
	    private String database_skills;

	    @Column
	    private String frameworks;
	    
	    @CreationTimestamp
	    private LocalDateTime modified_on;
	    
	    @Column
	    private Long modified_by;
}
