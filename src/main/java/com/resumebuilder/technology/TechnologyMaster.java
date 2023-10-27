package com.resumebuilder.technology;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.UpdateTimestamp;
import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
	@Column(name = "modified_by")
	private Long modified_by;
	@UpdateTimestamp
	private LocalDateTime modified_on;
	@Column(name="is_deleted", columnDefinition = "BOOLEAN DEFAULT false")
	private boolean is_deleted;
	
	@ManyToMany(mappedBy = "technologies") // Many technologies can be associated with many users
    private Set<User> users = new HashSet<>();
}
