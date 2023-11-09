package com.resumebuilder.projects;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resumebuilder.user.User;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Long >{

	void save(User user);
	
//	@Query("SELECT ep FROM EmployeeProject ep JOIN ep.users u WHERE u.user_id = :user_id")
//	List<EmployeeProject> findByUsersUserId(Long user_id);
	 @Query("SELECT e FROM EmployeeProject e WHERE is_deleted = :is_deleted AND e.emp_project_id= :emp_project_id")
	 EmployeeProject findByIsDeletedAndId(@Param("is_deleted") boolean isDeleted, @Param("emp_project_id") Long id);
	
	 @Query("SELECT e FROM EmployeeProject e WHERE is_deleted = :is_deleted")
	 List<EmployeeProject>findbyisdeleteMasters(@Param("is_deleted") boolean isDeleted);
}
