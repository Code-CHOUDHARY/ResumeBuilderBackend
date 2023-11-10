package com.resumebuilder.teamactivity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.resumebuilder.exception.UserNotFoundException;


public interface TeamActivityRepository extends JpaRepository<TeamActivity, Long>,CrudRepository<TeamActivity, Long>  {

	@Query(value="SELECT u.* \n"
			+ "FROM team_activity u \n"
			+ "INNER JOIN reporting_manager_mapping m ON m.employee_id = u.employee_id\n"
			+ "WHERE m.manager_employee_id =:managerId",nativeQuery = true)
	List<TeamActivity> findAllByManagers(@Param("managerId") Long managerId) ;
	
	
}
