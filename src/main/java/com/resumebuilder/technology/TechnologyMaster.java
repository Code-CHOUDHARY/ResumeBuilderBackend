package com.resumebuilder.technology;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.resumebuilder.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TechnologyMaster")
public class TechnologyMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long technology_id;
	private String technology_name;
	private String modified_by;
	private Date modified_on;
	private boolean is_deleted;
	
	@ManyToMany(mappedBy = "technologies") // Many technologies can be associated with many users
    private Set<User> users = new HashSet<>();
}
