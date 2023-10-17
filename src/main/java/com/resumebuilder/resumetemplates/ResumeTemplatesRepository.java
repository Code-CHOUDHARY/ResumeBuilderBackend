package com.resumebuilder.resumetemplates;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeTemplatesRepository extends JpaRepository<ResumeTemplates, Long>,CrudRepository<ResumeTemplates, Long> {
	 
	@Query(value="SELECT * FROM resume_template  WHERE is_deleted ='false'",nativeQuery = true)
	List<ResumeTemplates> findAllAvailable();
	
	
}
