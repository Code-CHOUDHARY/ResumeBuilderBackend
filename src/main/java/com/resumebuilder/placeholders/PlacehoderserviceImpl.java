package com.resumebuilder.placeholders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class PlacehoderserviceImpl implements PlaceholderService {

	@Autowired
	private ProfileRepo profilerepo;
	@Autowired
	private TechnologyRepo technologyrepo;
	
	
	@Autowired
	private ExperienceRepo experincerepo;

	@Autowired
	private ProjectRepo projectrepo;
	@Autowired
	private CertificateRepo certificaterepo;
	@Override
	public void addPlaceholder(PlaceholderRequestBody request) {
		// TODO Auto-generated method stub
		if(request!=null) {
			this.profilerepo.saveAll(request.getProfile());
			this.experincerepo.saveAll(request.getExperience());
			this.technologyrepo.saveAll(request.getTechnology());
			this.projectrepo.saveAll(request.getProject());
			this.certificaterepo.saveAll(request.getCertificates());
		}
		
	}
	@Override
	public PlaceholderRequestBody getplaceholder() {
		// TODO Auto-generated method stub
		PlaceholderRequestBody request=new PlaceholderRequestBody();
		request.setProfile(this.profilerepo.findAll());
		request.setTechnology(this.technologyrepo.findAll());
		request.setExperience(this.experincerepo.findAll());
		request.setProject(this.projectrepo.findAll());
		request.setCertificates(this.certificaterepo.findAll());
		
		return request;
	}


	
	
	
	
	}
