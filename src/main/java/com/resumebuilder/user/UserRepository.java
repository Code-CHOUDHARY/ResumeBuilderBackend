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
	
	@Query(value = "select * from user where email =:email AND is_deleted = false", nativeQuery = true)
	List<User> findByEmailIdAndNotDeleted(String email);
	//public User findByEmail_Id(String name);
	
	@Query(value = "select * from user where employee_id =:empId",nativeQuery = true)
	List<User> findByEmployeeId(String empId);
	
	@Query(value = "select * from user where employee_id =:empId",nativeQuery = true)
	User findByEmployeeIds(String empId);
	
	@Query(value = "SELECT * FROM user WHERE employee_id = :empId AND is_deleted = false", nativeQuery = true)
	List<User> findByEmployeeIdAndNotDeleted(String empId);
	  
	  @Query(value = "select * from user where email_id =:email",nativeQuery = true)
		User findByEmail_Id(String email);
		@Query(value = "select * from user where employee_id =:employeeId",nativeQuery = true)
		User findByEmployee_Id(String employeeId);
		
		@Query(value = "SELECT * FROM user u WHERE u.app_role_id = (SELECT id FROM app_roles WHERE name = 'ROLE_MANAGER')", nativeQuery = true)
	    List<User> findManagers();
		
		@Query(value = "SELECT * FROM user u WHERE u.app_role_id = (SELECT id FROM app_roles WHERE name = 'ROLE_USER')", nativeQuery = true)
	    List<User> findEmployees();

		//Optional<User> findById(User reportingManager);
//		Optional<User> findById(Long id);
		
//		  List<User> findByIdIn(List<Long> ids);
		
		@Query("SELECT u.full_name FROM User u WHERE u.user_id = :userId")
	    String findFullNameById(@Param("userId") Long userId);
		
//		@Query("SELECT u FROM User u WHERE u.user_id = :userId")

//		@Query("SELECT u FROM User u LEFT JOIN FETCH u.assignedProjects ap WHERE u.user_id = :userId")
//	    User findByUserId(@Param("userId") Long userId);
		
//		@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.employee_Id = :employeeId")
//	    boolean existsByEmployeeId(@Param("employeeId") String employeeId);
		
		@Query("select (count(u) > 0) from User u where u.email = ?1 and u.employee_Id = ?2")
		boolean existsByEmailAndEmployeeId(String email, String employee_Id);

		@Query("select (count(u) > 0) from User u where u.employee_Id = ?1 AND u.is_deleted = false")
		boolean existsByEmployeeId(String employee_Id);
		
		@Query("select (count(u) > 0) from User u where u.email = ?1 AND u.is_deleted = false")
		boolean existsByEmail(String email);
		
		@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.employee_Id = ?1 AND u.is_deleted = false")
		boolean existsByEmployeeIdAndNotDeleted(String employeeId);

		@Query("SELECT u FROM User u WHERE u.appRole.name = :roleName AND u.is_deleted = false")
	    boolean findByAppRoleNameAndNotDeleted(@Param("roleName") String string);
		
		@Query("SELECT u FROM User u WHERE u.email = :emailId AND u.employee_Id = :employeeId AND u.is_deleted = :isDeleted")
		User findByEmailIdAndEmployeeIdAndDeleted(
		    @Param("emailId") String emailId,
		    @Param("employeeId") String employeeId,
		    @Param("isDeleted") boolean isDeleted
		);

		
}