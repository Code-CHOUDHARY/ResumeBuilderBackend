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
@Table(name = "reporting_manager_allocation")
public class ReportingManager {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "employee_id") // Reference to the ROLE_USER employee
	    private User employee;

	    @ManyToOne
	    @JoinColumn(name = "manager_id") // Reference to the ROLE_MANAGER
	    private User manager;

}
