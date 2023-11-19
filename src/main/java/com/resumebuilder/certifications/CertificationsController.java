package com.resumebuilder.certifications;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.exception.CertificateNotFoundException;

@RestController
@RequestMapping("/api/certifications")
public class CertificationsController {

	@Autowired
	private CertificationsService certificationsService; // Autowired instance of CertificationsService for handling
	
	@Autowired
	private CertificationsRepository certificationRepository;
	
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
        @PathVariable Long certificationId, @RequestBody Certifications updatedCertifications, Principal principal)  {
        Certifications updatedCertification = certificationsService.updateCertifications(updatedCertifications, principal, certificationId );
        return new ResponseEntity<>(updatedCertification, HttpStatus.OK);
    }
    
 // Soft delete a certificate by its ID
    @DeleteMapping("/delete/{certificateId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public ResponseEntity<String> softDeleteCertificate(@PathVariable Long certificateId, Principal principal) {
        try {
            certificationsService.deleteCertificate(certificateId, principal);
            return new ResponseEntity<>("Certificate soft-deleted successfully", HttpStatus.OK);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while soft-deleting the certificate", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
 // Fetch certifications by employee ID
    @GetMapping("/list/{employeeId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public ResponseEntity<List<Certifications>> getCertificationsByEmployeeId(
        @PathVariable Long employeeId) {
    	
    	List<Certifications> certifications = certificationRepository.findCertificationsByEmployeeIdAndNotDeleted(employeeId);

        if (certifications.isEmpty()) {
        	throw new CertificateNotFoundException("No certifications found for the given employee ID");// Return 204 No Content if no certifications are found
        } else {
            return new ResponseEntity<>(certifications, HttpStatus.OK); // Return 200 OK with the certifications
        }
    }
    
 // Fetch a non-deleted certification by certification ID
    @GetMapping("/getById/{certificationId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public ResponseEntity<Certifications> getNonDeletedCertificationById(@PathVariable Long certificationId) {
    	Optional<Certifications> optionalCertification = certificationsService.getCertificationsById(certificationId);

        if (optionalCertification.isPresent()) {
            Certifications certification = optionalCertification.get();
            return new ResponseEntity<>(certification, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
