package com.resumebuilder.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesMappingRepository extends JpaRepository<UserRolesMapping, Long>{

	
}
