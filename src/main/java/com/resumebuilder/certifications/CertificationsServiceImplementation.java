package com.resumebuilder.certifications;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.CertificateNotFoundException;
import com.resumebuilder.exception.CertificationsNullException;
import com.resumebuilder.exception.ProjectDataNullException;
import com.resumebuilder.projects.ProjectMaster;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class CertificationsServiceImplementation implements CertificationsService{

	@Autowired
	private CertificationsRepository certificationsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
     * Add a new certification.
     *
     * @param The certificate to be added.
     * @principal represent user identity.
     * @return Save the certificate.
     */
	
	@Override
	public Certifications addCertifications(Certifications certifications, Principal principal) {
		
		User user = userRepository.findByEmail_Id(principal.getName());
		
		Certifications certificate = new Certifications();
		certificate.setCertificate_name(certifications.getCertificate_name());
		certificate.setCertificate_url(certifications.getCertificate_url());
		certificate.setCertificate_date(certifications.getCertificate_date());
		certificate.set_deleted(false);
		certificate.setShow_dates(false);
		certificate.setShow_duration(certifications.getShow_duration());
		certificate.setShow_nothing(false);
		certificate.setUser(user);
		
		// Check for null values or missing data in any field
        if (hasMissingData(certifications)) {
            throw new CertificationsNullException("Project data is incomplete or contains null values");
        }
		return certificationsRepository.save(certificate);
	}
	
	// Define a method to check for null values or missing data in any field
    private boolean hasMissingData(Certifications certifications) {
        return certifications.getCertificate_name() == null ||
        	   certifications.getCertificate_url() == null ||
        	   certifications.getCertificate_date() == null ||
        	   
               // Add checks for other fields as needed
               false; // Return false if all fields are non-null (no missing data)
    }

	@Override
	public Certifications updateCertifications(Certifications updatedCertifications, Principal principal, Long certificationId) {

	    // Find the existing certificate to update
	    Certifications existingCertificate = certificationsRepository.findById(certificationId)
	            .orElseThrow(() -> new CertificateNotFoundException("Certificate not found"));

	    // Check for null values or missing data in the updated certificate
	    if (hasAnyMissingData(updatedCertifications)) {
	        throw new CertificationsNullException("Updated certificate data is incomplete or contains null values");
	    }

	    // Update the fields of the existing certificate with the new data
	    existingCertificate.setCertificate_name(updatedCertifications.getCertificate_name());
	    existingCertificate.setCertificate_url(updatedCertifications.getCertificate_url());
	    existingCertificate.setCertificate_date(updatedCertifications.getCertificate_date());
	    existingCertificate.setShow_duration(updatedCertifications.getShow_duration());

	    // Save the updated certificate
	    return certificationsRepository.save(existingCertificate);
	}

	// Define a method to check for null values or missing data in any field
	private boolean hasAnyMissingData(Certifications certifications) {
	    return certifications.getCertificate_name() == null ||
	           certifications.getCertificate_url() == null ||
	           certifications.getCertificate_date() == null ||
	           // Add checks for other fields as needed
	           false;
	}

}
