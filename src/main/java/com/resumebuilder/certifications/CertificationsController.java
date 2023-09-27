package com.resumebuilder.certifications;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/certifications")
public class CertificationsController {
	
//	@Autowired
//	private CertificationsService certificationsService; // Autowired instance of CertificationsService for handling business logic
//	
//	// This method is used to add a new certification for an employee
//	@PostMapping("/add")
//	@PreAuthorize("hasRole('Employee')") // Requires the 'Employee' role to access this endpoint
//    public ResponseEntity<Certifications> addRole(@RequestBody Certifications certifications,Principal principal) {
//		// Call the CertificationsService to add the certification, passing the certification object and principal
//		Certifications savedRole = certificationsService.addCertifications(certifications, principal);
//		// Return a ResponseEntity with the saved certification and HTTP status code 201 (Created)
//		return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
//    }
	

}
