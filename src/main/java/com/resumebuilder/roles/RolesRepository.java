package com.resumebuilder.roles;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


//This interface extends JpaRepository, which provides CRUD (Create, Read, Update, Delete) operations
// for the 'Roles' entity. It is a Spring Data JPA repository.

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long>{

	@Query(value = "select * from roles where role_name =:role_name",nativeQuery = true)
	Roles findByRoleName(String role_name);
	
	@Query(value = "select * from roles where role_name =:role_name",nativeQuery = true)
	List<Roles> findByRolesName(String role_name);
	
//	@Query("SELECT r FROM Roles r WHERE r.role_name = :roleName AND r.is_deleted = false")
//    Roles findActiveRoleByName(@Param("roleName") String roleName);
//
//    @Query("SELECT r FROM Roles r WHERE r.role_name = :roleName AND r.is_deleted = true")
//    Roles findSoftDeletedRoleByName(@Param("roleName") String roleName);


}
