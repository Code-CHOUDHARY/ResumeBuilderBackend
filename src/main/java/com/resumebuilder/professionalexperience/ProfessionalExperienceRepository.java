package com.resumebuilder.professionalexperience;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalExperienceRepository extends JpaRepository<ProfessionalExperience, Long> {
	
	 @Query("SELECT ex FROM ProfessionalExperience ex WHERE ex.user.is_deleted = false AND ex.user.user_id = :user_id")
	 List<ProfessionalExperience> findByUserId(String user_id);
	 @Query(value="select sum(TIMESTAMPDIFF( month,start_date,end_date)) As TotalExperience from professional_experience \n"
			 + "where employee_id=? group by employee_id;",nativeQuery = true)
	 public Integer getTotalExperience(String empId);
}
