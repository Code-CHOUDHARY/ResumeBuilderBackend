package com.resumebuilder.placeholders;

import java.util.List;

public interface PlaceholderService {

//	public void addProfilePlaceholder(List<Profile> profiles);
//	
//	public void addExperiencePlaceholder(List<Experience>experience);
//	
//	public void addTechnologyPlaceholder(List<Technology>technologies);
//	public void addProjectPlaceholder(List<Projects>projects);
//	public void addCertificatePlaceholder(List<Certificate>certificates);

	
	void addPlaceholder(PlaceholderRequestBody request);
	
	PlaceholderMapping getallmappedPlaceholders();
	PlaceholderRequestBody getplaceholder();
}
