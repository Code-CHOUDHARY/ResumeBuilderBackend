package com.resumebuilder.education;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resumebuilder.user.User;


@Repository
public interface EducationRepository extends JpaRepository<Education, Long>{
	
	@Query(value = "select * from Education where degree =:degree",nativeQuery = true)
	Education findByDegreeName(String degree);

	Education findByDegree(String degree);
	
	 @Query("SELECT e FROM Education e WHERE e.user.id = :userId AND e.is_deleted = false")
	 List<Education> findActiveEducationsForUser(@Param("userId") Long userId);
	 
	 @Query("SELECT e FROM Education e WHERE e.user = :user AND e.degree = :degree")
	 Education findByUserAndDegree(@Param("user") User user, @Param("degree") String degree);

	 @Query("SELECT e FROM Education e WHERE e.degree = :degree AND e.user = :user")
	 Education findByDegreeAndUser(@Param("degree") String degree, @Param("user") User user);
	 
	 @Query("SELECT e FROM Education e WHERE e.degree = :degree AND e.user = :user AND e.is_deleted = true")
	 Education findSoftDeletedByDegreeAndUser(@Param("degree") String degree, @Param("user") User user);

}
