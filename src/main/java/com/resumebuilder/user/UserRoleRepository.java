package com.resumebuilder.user;

import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.approle.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    @Query("select u from UserRole u where u.name = ?1")
    UserRole findByName(ERole name);


}