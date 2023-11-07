package com.resumebuilder.projects;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//This interface extends JpaRepository, which provides CRUD (Create, Read, Update, Delete) operations
// for the 'ProjectMaster' entity. It is a Spring Data JPA repository.

@Repository
public interface ProjectMasterRepository extends JpaRepository<ProjectMaster, Long> {

	//ProjectMaster findByProjectTitle(String project_title);
	 @Query("SELECT e FROM ProjectMaster e WHERE is_deleted = :is_deleted AND e.project_master_id = :project_master_id")
	 ProjectMaster findByIsDeletedAndId(@Param("is_deleted") boolean isDeleted, @Param("project_master_id") Long id);
	 @Query("SELECT e FROM ProjectMaster e WHERE is_deleted = :is_deleted")
	 List<ProjectMaster>findbyisdeleteMasters(@Param("is_deleted") boolean isDeleted);
}
