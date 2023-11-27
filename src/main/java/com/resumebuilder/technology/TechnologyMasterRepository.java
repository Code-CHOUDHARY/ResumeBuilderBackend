package com.resumebuilder.technology;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyMasterRepository extends JpaRepository<TechnologyMaster, Long>{

	@Query(value = "select * from technology_master where technology_name =:technology_name",nativeQuery = true)
	TechnologyMaster findByTechnologyName(String technology_name);
	
	@Query(value = "select * from technology_master where technology_name =:technology_name",nativeQuery = true)
	List<TechnologyMaster> findByTechnologiesName(String technology_name);
	
	@Query("SELECT ep FROM TechnologyMaster ep JOIN ep.users u WHERE u.user_id = :user_id")
	List<TechnologyMaster> findByUsersUserId(Long user_id);
	
	@Query("SELECT t.technology_name FROM TechnologyMaster t WHERE LOWER(t.technology_name) LIKE LOWER(CONCAT(?1, '%'))")
    List<String> findSkillsStartingWith(String input);
	
	@Query("SELECT t FROM TechnologyMaster t WHERE t.is_deleted = false")
    List<TechnologyMaster> findActiveTechnologies();
	

}
