package com.resumebuilder.bulkupload;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.resumebuilder.DTO.TechnologyDto;
import com.resumebuilder.technology.TechnologyMaster;
import com.resumebuilder.technology.TechnologyMasterRepository;

@Service
public class TechnologyExcelService {
	
	@Autowired
	private TechnologyMasterRepository technologyRepository;
	
	public void save(MultipartFile file) throws IOException {
        List<TechnologyDto> technology = TechnologyExcelHelper.convertExcelToListofTechnology(file.getInputStream());
        for (TechnologyDto technologies : technology) {
            // Check if a record with the same technology name already exists
        	TechnologyMaster existingtechnology = technologyRepository.findByTechnologyName(technologies.getTechnology_name());
            
        	TechnologyMaster technologySave;
            
            if (existingtechnology != null) {
                // If the record exists, update it
            	technologySave = existingtechnology;
            } else {
                // If the record does not exist, create a new one
            	technologySave = new TechnologyMaster();
            }
            
            technologySave.setTechnology_name(technologies.getTechnology_name());
            
            // Save the role 
            technologyRepository.save(technologySave);
        }
    }
    
    public List<TechnologyMaster> getAllTechnologies() {
        return this.technologyRepository.findAll();
    }
	

}
