package com.resumebuilder.certifications;

import java.security.Principal;

//service interface for required method according to functionality.
public interface CertificationsService {
	public Certifications addCertifications(Certifications certifications, Principal principal);
	public Certifications updateCertifications(Certifications updatedCertifications, Principal principal, Long certificate_id);	
}
