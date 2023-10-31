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
	

}
