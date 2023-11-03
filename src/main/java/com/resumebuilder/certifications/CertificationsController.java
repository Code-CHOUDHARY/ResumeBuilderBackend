package com.resumebuilder.certifications;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/certifications")
public class CertificationsController {

	@Autowired
	private CertificationsService certificationsService; // Autowired instance of CertificationsService for handling
															// business logic

	// This method is used to add a new certification for an employee
	@PostMapping("/add")
	@PreAuthorize("hasAnyRole('MANAGER', 'USER')") // Requires the 'Employee' role to access this endpoint
	public ResponseEntity<Certifications> addCertifications(@RequestBody Certifications certifications, Principal principal) {
		// Call the CertificationsService to add the certification, passing the
		// certification object and principal
		Certifications savedRole = certificationsService.addCertifications(certifications, principal);
		// Return a ResponseEntity with the saved certification and HTTP status code 201
		// (Created)
		return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
	}
	
	
	 // Update an existing certification for an employee
    @PutMapping("/update/{certificationId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public ResponseEntity<Certifications> updateCertifications(
        @PathVariable Long certificationId, @RequestBody Certifications updatedCertifications, Principal principal) {
        Certifications updatedCertification = certificationsService.updateCertifications(updatedCertifications, principal, certificationId );
        return new ResponseEntity<>(updatedCertification, HttpStatus.OK);
    }
}
