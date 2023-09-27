package com.resumebuilder.certifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//This interface extends JpaRepository, which provides CRUD (Create, Read, Update, Delete) operations
// for the 'Certifications' entity. It is a Spring Data JPA repository.

@Repository
public interface CertificationsRepository extends JpaRepository<Certifications, Long>{

}
