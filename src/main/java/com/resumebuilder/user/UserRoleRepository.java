package com.resumebuilder.user;

import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.approle.UserRole;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    @Query("select u from UserRole u where u.name = ?1")
    UserRole findByName(ERole name);

//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.appRole.name = :roleName")
//	boolean findByRoleName(@Param("roleName") ERole roleName);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.appRole.name = :roleName")
	boolean existsByRoleName(@Param("roleName") ERole roleAdmin);

}