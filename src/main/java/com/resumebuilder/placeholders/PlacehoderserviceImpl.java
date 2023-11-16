package com.resumebuilder.placeholders;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private static <T> Map<String, String> convertToMap(List<T> list, String keyField, String valueField) {
        Map<String, String> map = new HashMap<>();
        for (T item : list) {
            try {
                Field key = item.getClass().getDeclaredField(keyField);
                Field value = item.getClass().getDeclaredField(valueField);

                // Make private fields accessible
                key.setAccessible(true);
                value.setAccessible(true);

                String keyStr = (String) key.get(item);
                String valueStr = (String) value.get(item);
                map.put(keyStr, valueStr);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace(); // Handle the exception as needed
            }
        }
        return map;
    }
	@Override
	public PlaceholderMapping getallmappedPlaceholders() {
		// TODO Auto-generated method stub
		PlaceholderRequestBody request=new PlaceholderRequestBody();
		request.setProfile(this.profilerepo.findAll());
		request.setTechnology(this.technologyrepo.findAll());
		request.setExperience(this.experincerepo.findAll());
		request.setProject(this.projectrepo.findAll());
		request.setCertificates(this.certificaterepo.findAll());
		
		PlaceholderMapping placeholder=new PlaceholderMapping();
		Map<String, String> profileMap = convertToMap(request.getProfile(), "name", "placeholder");
        Map<String, String> technologyMap = convertToMap(request.getTechnology(), "name", "placeholder");
        Map<String, String> experienceMap = convertToMap(request.getExperience(), "name", "placeholder");
        Map<String, String> projectMap = convertToMap(request.getProject(), "name", "placeholder");
        Map<String, String> certificateMap = convertToMap(request.getCertificates(), "name", "placeholder");
        System.out.println("placeholder request=="+profileMap.get("Years of Experience"));
      
        placeholder.setProfileMap(profileMap);
        placeholder.setTechnologyMap(technologyMap);
        placeholder.setExperienceMap(experienceMap);
        placeholder.setProjectMap(projectMap);
        placeholder.setCertificateMap(certificateMap);
        
        
		return placeholder;
	}
	
	
	
	
	
	}
