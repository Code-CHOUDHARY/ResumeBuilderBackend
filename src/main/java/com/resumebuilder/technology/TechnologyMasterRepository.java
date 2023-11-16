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
	
	/**
	 * Custom query to retrieve technology names from the TechnologyMaster entity
	 * that start with a given input character (case-insensitive).
	 *
	 * @param input The input character for matching technology names.
	 * @return A list of technology names matching the input character.
	 */
	@Query("SELECT ep FROM TechnologyMaster ep JOIN ep.users u WHERE u.user_id = :user_id")
	List<TechnologyMaster> findByUsersUserId(Long user_id);
	@Query("SELECT t.technology_name FROM TechnologyMaster t WHERE LOWER(t.technology_name) LIKE LOWER(CONCAT(?1, '%'))")
    List<String> findSkillsStartingWith(String input);
	

}
