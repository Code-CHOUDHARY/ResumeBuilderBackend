package com.resumebuilder.placeholders;

import java.util.List;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceholderRequestBody
{
private List<ProfilePlaceholder>profile;
private List<TechnologyPlaceholder>technology;
private List<ExperiencePlaceholder>experience;
private List<ProjectsPlaceholder>project;
private List<CertificatePlaceholder>certificates;

	
	
}
