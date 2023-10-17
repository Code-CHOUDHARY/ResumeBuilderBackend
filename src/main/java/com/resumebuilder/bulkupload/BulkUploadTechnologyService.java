package com.resumebuilder.bulkupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resumebuilder.exception.DataProcessingException;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.technology.TechnologyMaster;
import com.resumebuilder.technology.TechnologyMasterRepository;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class BulkUploadTechnologyService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TechnologyMasterRepository technologyMasterRepository;
	
	@Autowired
    private Map<Integer, String> technologyColumnMapping;
	
	public void processTechnologyExcelFile(MultipartFile file, Principal principal) throws IOException, java.io.IOException {
	    
		
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
	        Sheet technologySheet = workbook.getSheet("Technologies");

	        User user = userRepository.findByEmailId(principal.getName());

	        List<String> validationMessages = validateTechnologySheet(technologySheet);

	        if (!validationMessages.isEmpty()) {
	            throw new DataProcessingException(String.join(", ", validationMessages));
	        }

	        // Continue with saving the data
	        processTechnologySheet(technologySheet, user);
	    } catch (java.io.IOException e) {
            e.printStackTrace();
        }
     
	}

	private List<String> validateTechnologySheet(Sheet sheet) {
	    List<String> validationMessages = new ArrayList<>();
	    Set<String> uniqueRoleNames = new HashSet<>();

	    for (Row row : sheet) {
	        if (row.getRowNum() == 0) {
	            // Skip the header row
	            continue;
	        }

	        for (Map.Entry<Integer, String> entry : technologyColumnMapping.entrySet()) {
	            Integer columnIndex = entry.getKey();
	            String fieldName = entry.getValue();
	            String cellValue = getStringValue(row.getCell(columnIndex));

	            if (fieldName.equals("technology_name")) {
	                // Check for duplicate technology names
	                if (uniqueRoleNames.contains(cellValue)) {
	                    validationMessages.add("Duplicate data entry for technology name: " + cellValue);
	                } else {
	                    uniqueRoleNames.add(cellValue);
	                }

	                // Check for missing values
	                if (cellValue == null || cellValue.isEmpty()) {
	                    validationMessages.add("Data missing for technology name");
	                }
	            }
	        }
	    }

	    return validationMessages;
	}

	private void processTechnologySheet(Sheet sheet, User currentUser) {
		List<TechnologyMaster> existingTechnology = technologyMasterRepository.findAll();
	    for (Row row : sheet) {
	        if (row.getRowNum() == 0) {
	            // Skip the header row
	            continue;
	        }

	        TechnologyMaster technologies = new TechnologyMaster();
	        technologies.setModified_by(currentUser.getFull_name());

	        for (Map.Entry<Integer, String> entry : technologyColumnMapping.entrySet()) {
	            Integer columnIndex = entry.getKey();
	            String fieldName = entry.getValue();
	            String cellValue = getStringValue(row.getCell(columnIndex));

	            setFieldValue(technologies, fieldName, cellValue);
	        }
	        	
	        TechnologyMaster existingTechnologies = findTechnologyByName(existingTechnology, technologies.getTechnology_name());
	        	 if (existingTechnologies == null) {
	                 // technology doesn't exist, so create a new one
	        		 technologies.setModified_by(currentUser.getFull_name());
	                 technologyMasterRepository.save(technologies);
	             } else if (existingTechnologies.is_deleted()) {
	            	 //check existing soft delete technology and re-add again
	            	 TechnologyMaster newTech = new TechnologyMaster();
	            	 newTech.setTechnology_name(technologies.getTechnology_name());
	            	 newTech.setModified_by(currentUser.getFull_name());
	            	 newTech.set_deleted(false);
	            	 technologyMasterRepository.save(newTech);
	            	
	             }
	        	
	        }
	       
	    }
	    
	    
	
	
	private TechnologyMaster findTechnologyByName(List<TechnologyMaster> technologyList, String technologyName) {
	    for (TechnologyMaster tech : technologyList) {
	        if (tech.getTechnology_name().equals(technologyName) && tech.is_deleted()==false) {
	            return tech;
	        }
	    }
	    return null;
	}
    
    private String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            default:
                return null;
        }
    }

    private void setFieldValue(TechnologyMaster technology, String fieldName, String cellValue) {
        try {
            Field field = technology.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (field.getType() == String.class) {
                field.set(technology, cellValue);
            } else if (field.getType() == Integer.class) {
                field.set(technology, Integer.parseInt(cellValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
