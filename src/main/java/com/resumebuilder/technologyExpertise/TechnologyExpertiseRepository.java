package com.resumebuilder.technologyExpertise;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.resumebuilder.DTO.TechnologyExpertiseDto;
import com.resumebuilder.user.User;


@Repository
public interface TechnologyExpertiseRepository extends JpaRepository< TechnologyExpertise, Long>{
	
	 @Query("SELECT te FROM TechnologyExpertise te WHERE te.user.user_id = :user_id")
	  TechnologyExpertise findByUserId(@Param("user_id") Long user_id);
	
}
