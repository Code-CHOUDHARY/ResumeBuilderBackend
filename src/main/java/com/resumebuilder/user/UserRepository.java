package com.resumebuilder.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;




@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.id IN :user")
    public List<User> findAllUserByUserIds(@Param("user") List<Integer> userIds);
	
	
	
	//public User findByEmail_Id(String name);
//	Optional<User> findByEmail(String email);
//	  Optional<User> findByUsername(String username);
//
//	  Boolean existsByUsername(String username);

	  Boolean existsByEmail(String email);
}
