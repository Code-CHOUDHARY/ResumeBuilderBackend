package com.resumebuilder.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//This interface extends JpaRepository, which provides CRUD (Create, Read, Update, Delete) operations
// for the 'ProjectMaster' entity. It is a Spring Data JPA repository.

@Repository
public interface ProjectMasterRepository extends JpaRepository<ProjectMaster, Long> {

	//ProjectMaster findByProjectTitle(String project_title);

}
