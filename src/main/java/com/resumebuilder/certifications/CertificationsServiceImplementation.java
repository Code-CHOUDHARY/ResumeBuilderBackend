package com.resumebuilder.certifications;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.exception.CertificateNotFoundException;
import com.resumebuilder.exception.CertificationsNullException;
import com.resumebuilder.exception.ProjectDataNullException;
import com.resumebuilder.professionalexperience.JsonConverter;
import com.resumebuilder.projects.ProjectMaster;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import io.jsonwebtoken.lang.Objects;

@Service
public class CertificationsServiceImplementation implements CertificationsService{

	@Autowired
	private CertificationsRepository certificationsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ActivityHistoryService activityHistoryService;
	
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
		
		 ActivityHistory activityHistory = new ActivityHistory();
      	 String newData;
		try {
			 newData = JsonConverter.convertToJson(certifications);
			 activityHistory.setActivity_type("Add Certificate");	            
	         activityHistory.setDescription("Change in Certifications data");
	         activityHistory.setNew_data(newData);
	         activityHistory.setUser(user);
	         activityHistoryService.addActivity(activityHistory, principal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
		
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
	public Certifications updateCertifications(Certifications updatedCertifications, Principal principal, Long certificationId)  {
		
		User user = userRepository.findByEmail_Id(principal.getName());
		
	    // Find the existing certificate to update
	    Certifications existingCertificate = certificationsRepository.findById(certificationId)
	            .orElseThrow(() -> new CertificateNotFoundException("Certificate not found"));
	    
	    ActivityHistory activityHistory = new ActivityHistory();
	    String oldData;
		try {
			oldData = JsonConverter.convertToJson(existingCertificate);
			activityHistory.setOld_data(oldData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    // Check for null values or missing data in the updated certificate
	    if (hasAnyMissingData(updatedCertifications)) {
	        throw new CertificationsNullException("Updated certificate data is incomplete or contains null values");
	    }
	    
	    Map<String, String> changes = new HashMap<>();
        if (!Objects.nullSafeEquals(existingCertificate.getCertificate_name(), updatedCertifications.getCertificate_name())) {
            changes.put("Certificate name", updatedCertifications.getCertificate_name());
        }
        if (!Objects.nullSafeEquals(existingCertificate.getCertificate_url(), updatedCertifications.getCertificate_url())) {
            changes.put("Certificate url", updatedCertifications.getCertificate_url());
        }
        if (!Objects.nullSafeEquals(existingCertificate.getCertificate_date(), updatedCertifications.getCertificate_date())) {
            changes.put("Certificate date", updatedCertifications.getCertificate_date());
        }
        if (!Objects.nullSafeEquals(existingCertificate.getShow_duration(), updatedCertifications.getShow_duration())) {
            changes.put("Duration", updatedCertifications.getShow_duration());
        }

	    // Update the fields of the existing certificate with the new data
	    existingCertificate.setCertificate_name(updatedCertifications.getCertificate_name());
	    existingCertificate.setCertificate_url(updatedCertifications.getCertificate_url());
	    existingCertificate.setCertificate_date(updatedCertifications.getCertificate_date());
	    existingCertificate.setShow_duration(updatedCertifications.getShow_duration());
	        	    
		try {
			String newData = JsonConverter.convertToJson(changes);
			 activityHistory.setActivity_type("Update Certificate");	            
	         activityHistory.setDescription("Change in Certifications data");
	         activityHistory.setNew_data(newData);
	         activityHistory.setUser(user);
	         activityHistoryService.addActivity(activityHistory, principal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
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
	
	@Override
	public void deleteCertificate(Long certificateId, Principal principal) {
		
		User user = userRepository.findByEmail_Id(principal.getName());
		
		Certifications certificate = certificationsRepository.findById(certificateId)
                .orElseThrow(() -> new CertificateNotFoundException("Certificate not found"));

        // Check if the user is authorized to delete this certificate (e.g., based on user ID or role)
        if (!isAuthorizedToDeleteCertificate(certificate, principal)) {
            throw new CertificateNotFoundException("You are not authorized to delete this certificate");
        }
        
        // Set the 'deleted' flag to true to mark it as soft-deleted
        certificate.set_deleted(true);
        
        ActivityHistory activityHistory = new ActivityHistory();
     	String newData;
		try {
			 activityHistory.setActivity_type("Delete Certificate");	            
	         activityHistory.setDescription("Change in Certifications data");
	         activityHistory.setNew_data("Certificate with id " + certificateId + "is deleted");
	         activityHistory.setUser(user);
	         activityHistoryService.addActivity(activityHistory, principal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Save the updated certificate
        certificationsRepository.save(certificate);
    }

    // You may need to implement a method to check if the user is authorized to delete the certificate
    private boolean isAuthorizedToDeleteCertificate(Certifications certificate, Principal principal) {
        // Implement your authorization logic here (e.g., based on user roles or ownership)
        // For example, you can compare the certificate's user with the authenticated user.
        User user = userRepository.findByEmail_Id(principal.getName());
        return certificate.getUser().equals(user);
    }

	@Override
	public Optional<Certifications> getCertificationsById(Long certificationId) {
		// TODO Auto-generated method stub
		
		 Optional<Certifications> certifications = certificationsRepository.findByCertification_id(certificationId);
		 if (certifications.isPresent()) {
	            return Optional.of(certifications.get());
	        } else {
	            // Handle the case where no certification is found
	            throw new CertificateNotFoundException("certification not found for the given certification ID");
	        }
	}


}
