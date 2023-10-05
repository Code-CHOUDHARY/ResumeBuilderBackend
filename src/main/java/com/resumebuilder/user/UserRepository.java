package com.resumebuilder.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	@Query(value = "select * from user where email_id =:email",nativeQuery = true)
	List<User> findByEmailIds(String email);
	
	User findByEmail(String email);
	//public User findByEmail_Id(String name);
	
	@Query(value = "select * from user where employee_id =:empId",nativeQuery = true)
	List<User> findByEmployeeId(String empId);
	
	@Query(value = "select * from user where employee_id =:empId",nativeQuery = true)
	User findByEmployeeIds(String empId);
	

}
