package com.resumebuilder.reportingmanager;

import com.resumebuilder.user.User;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reporting_manager_mapping")
public class ReportingManager {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	 	@OneToOne
		@JoinColumn(name = "employee_id")
	    private User employee_id;

	    @ManyToOne
	    @JoinColumn(name = "manager_employee_id")
	    private User manager_employee_id;

}
