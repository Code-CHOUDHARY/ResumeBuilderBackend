package com.resumebuilder.certifications;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//This interface extends JpaRepository, which provides CRUD (Create, Read, Update, Delete) operations
// for the 'Certifications' entity. It is a Spring Data JPA repository.

@Repository
public interface CertificationsRepository extends JpaRepository<Certifications, Long>{
	// Custom query to find certifications by employee ID
    @Query("SELECT c FROM Certifications c WHERE c.user.user_id = :employee_id AND c.is_deleted = false")
    List<Certifications> findCertificationsByEmployeeIdAndNotDeleted(Long employee_id);
    @Query("SELECT c FROM Certifications c WHERE c.certification_id = :certification_id AND c.is_deleted = false")
    Optional<Certifications> findByCertification_id(Long certification_id);
}
