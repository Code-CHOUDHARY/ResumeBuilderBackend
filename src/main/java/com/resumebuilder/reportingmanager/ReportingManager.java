package com.resumebuilder.reportingmanager;

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
@Table(name = "reporting_manager_mapping")
public class ReportingManager {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

//	 	@ManyToOne
//	    @JoinColumn(name = "employee_id")
	    private String employee_id;

//	    @ManyToOne
//	    @JoinColumn(name = "manager_employee_id")
	    private String manager_employee_id;

}
