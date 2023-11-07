package com.resumebuilder.professionalexperience;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfessionalExperienceRepository extends JpaRepository<ProfessionalExperience, Long>,CrudRepository<ProfessionalExperience, Long>{

	@Query(value="select sum(TIMESTAMPDIFF( month,start_date,end_date)) As TotalExperience from professional_experience \n"
			+ "where employee_id=? group by employee_id;",nativeQuery = true)
	public Integer getTotalExperience(String empId);
}
