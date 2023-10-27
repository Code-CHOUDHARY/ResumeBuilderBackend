package com.resumebuilder.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.approle.UserRole;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{


	Optional<User> findByEmail(String email);
	
//	@Query(value = "select * from user where email_id =:email",nativeQuery = true)
//	User findByEmployeeEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.id IN :user")
    public List<User> findAllUserByUserIds(@Param("user") List<Integer> userIds);
	
	
	
	//public User findByEmail_Id(String name);
//	Optional<User> findByEmail(String email);
//	  Optional<User> findByUsername(String username);
//
//	  Boolean existsByUsername(String username);

	@Query(value = "select * from user where email_id =:email",nativeQuery = true)
	List<User> findByEmailIds(String email);
	
	@Query(value = "select * from user where email_id =:email",nativeQuery = true)
	User findByEmailId(String email);
	//public User findByEmail_Id(String name);
	
	@Query(value = "select * from user where employee_id =:empId",nativeQuery = true)
	List<User> findByEmployeeId(String empId);
	
	@Query(value = "select * from user where employee_id =:empId",nativeQuery = true)
	User findByEmployeeIds(String empId);
	

	  Boolean existsByEmail(String email);
	  
	  @Query(value = "select * from user where email_id =:email",nativeQuery = true)
		User findByEmail_Id(String email);
		@Query(value = "select * from user where employee_id =:employeeId",nativeQuery = true)
		User findByEmployee_Id(String employeeId);
		
		@Query(value = "SELECT * FROM user u WHERE u.app_role_id = (SELECT id FROM app_roles WHERE name = 'ROLE_MANAGER')", nativeQuery = true)
	    List<User> findManagers();

		//Optional<User> findById(User reportingManager);
		Optional<User> findById(Long id);
		
		
		@Query("SELECT u.full_name FROM User u WHERE u.user_id = :userId")
	    String findFullNameById(@Param("userId") Long userId);
		
}
