package com.resumebuilder.reportingmanager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resumebuilder.user.User;

@Repository
public interface ReportingManagerRepository extends JpaRepository<ReportingManager, Long> {

	@Query(value = "select * from reporting_manager_mapping where employee_id =:employee_id",nativeQuery = true)
	ReportingManager findByEmployee_id(String employee_id);
	@Query(value = "select * from reporting_manager_mapping where employee_id =:manager_employee_id",nativeQuery = true)
    ReportingManager findByManager_employee_id(String manager_employee_id);
	
	//List<ReportingManager> findByEmployee_id(User userSave);

	//List<ReportingManager> findByEmployee_id(User user);
}
