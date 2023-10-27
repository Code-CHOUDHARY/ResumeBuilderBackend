package com.resumebuilder.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectAssignRepository extends JpaRepository<ProjectAssign, Long>{

}
