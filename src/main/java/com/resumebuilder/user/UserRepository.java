package com.resumebuilder.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.resumebuilder.DTO.UserDto;
import com.resumebuilder.auth.PasswordResetToken;
import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.approle.UserRole;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{


	@Query("SELECT u FROM User u WHERE u.email = :email AND u.is_deleted = false")
	Optional<User> findByEmail(String email);
//	@Query("SELECT DISTINCT u FROM User u " +
//	           "LEFT JOIN FETCH u.employeeProject p " +
//			"WHERE u.email = :email AND u.is_deleted = false AND p.is_deleted = false")
//	    Optional<User> findByEmail(String email);
//	@Query(value = "select * from user where email_id =:email",nativeQuery = true)
//	User findByEmployeeEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.id IN :user")
    public List<User> findAllUserByUserIds(@Param("user") List<Integer> userIds);
	@Query("select (count(u) > 0) from User u where u.employee_Id = ?1 AND u.is_deleted = false")
	boolean existsByEmployeeId(String employee_Id);
	
	@Query("select (count(u) > 0) from User u where u.email = ?1 AND u.is_deleted = false")
	boolean existsByEmail(String email);
	
	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.employee_Id = ?1 AND u.is_deleted = false")
	boolean existsByEmployeeIdAndNotDeleted(String employeeId);
	
	//public User findByEmail_Id(String name);
//	Optional<User> findByEmail(String email);
//	  Optional<User> findByUsername(String username);
//
//	  Boolean existsByUsername(String username);

	@Query(value = "select * from user where email_id =:email",nativeQuery = true)
	List<User> findByEmailIds(String email);
	
	@Query(value = "select * from user where email_id =:email",nativeQuery = true)
	User findByEmailId(String email);
	
	@Query(value = "select * from user where email =:email AND is_deleted = false", nativeQuery = true)
	List<User> findByEmailIdAndNotDeleted(String email);
	//public User findByEmail_Id(String name);
	
	@Query(value = "select * from user where employee_id =:empId",nativeQuery = true)
	List<User> findByEmployeeId(String empId);
	
	@Query(value = "select * from user where employee_id =:empId",nativeQuery = true)
	User findByEmployeeIds(String empId);
	  
	  @Query(value = "select * from user where email_id =:email",nativeQuery = true)
		User findByEmail_Id(String email);
		@Query(value = "select * from user where employee_id =:employeeId",nativeQuery = true)
		User findByEmployee_Id(String employeeId);
		
		@Query(value = "SELECT * FROM user u WHERE u.app_role_id = (SELECT id FROM app_roles WHERE name = 'ROLE_MANAGER')", nativeQuery = true)
	    List<User> findManagers();
		
		@Query(value = "SELECT * FROM user u WHERE u.app_role_id = (SELECT id FROM app_roles WHERE name = 'ROLE_USER')", nativeQuery = true)
	    List<User> findEmployees();
		
		@Query("SELECT u.full_name FROM User u WHERE u.user_id = :userId")
	    String findFullNameById(@Param("userId") Long userId);
				
		//@Query("SELECT u FROM User u WHERE u.is_deleted = false")   //fetch all users as per all app role user
		@Query(value = "SELECT * FROM user u WHERE u.app_role_id IN (SELECT id FROM app_roles WHERE name IN ('ROLE_USER', 'ROLE_MANAGER')) AND u.is_deleted = false", nativeQuery = true)
	    List<User> getAllActiveUsers();
		
		 

}
