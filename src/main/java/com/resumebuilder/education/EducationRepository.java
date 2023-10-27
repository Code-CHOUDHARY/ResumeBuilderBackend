package com.resumebuilder.education;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EducationRepository extends JpaRepository<Education, Long>{
	
	@Query(value = "select * from Education where degree =:degree",nativeQuery = true)
	Education findByDegreeName(String degree);

}
