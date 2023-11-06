package com.resumebuilder.certifications;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

//service interface for required method according to functionality.
public interface CertificationsService {
	public Certifications addCertifications(Certifications certifications, Principal principal);
	public Certifications updateCertifications(Certifications updatedCertifications, Principal principal, Long certificate_id);
	public void deleteCertificate(Long certificateId, Principal principal);
	public Optional<Certifications> getCertificationsById(Long certificationId);
}
