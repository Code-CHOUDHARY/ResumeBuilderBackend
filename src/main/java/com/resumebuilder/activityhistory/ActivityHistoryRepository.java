package com.resumebuilder.activityhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.resumebuilder.DTO.TeamActivityDto;
import com.resumebuilder.user.User;


public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {
		
	List<ActivityHistory> findByUser(User user);
	
	
	@Query(value = "SELECT a.* FROM activity_history a " +
	           "INNER JOIN reporting_manager_allocation rma ON rma.employee_id = a.activity_for " +
	           "WHERE rma.manager_id = :manager_id ",nativeQuery = true)
	    List<ActivityHistory> findActivitiesByReportingManager(Long manager_id);
}

//@Query(value="SELECT u.* \n"
//		+ "FROM team_activity u \n"
//		+ "INNER JOIN reporting_manager_mapping m ON m.employee_id = u.employee_id\n"
//		+ "WHERE m.manager_employee_id =:managerId",nativeQuery = true)