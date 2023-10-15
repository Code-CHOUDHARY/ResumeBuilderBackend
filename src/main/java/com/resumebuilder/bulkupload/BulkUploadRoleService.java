package com.resumebuilder.bulkupload;

import java.io.File;
import java.lang.reflect.Field;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
import org.springframework.core.env.Environment;
import com.resumebuilder.exception.DataProcessingException;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class BulkUploadRoleService {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
    private Map<Integer, String> rolesColumnMapping; // Inject the mapping for Roles
	
	public void processRoleExcelFile(MultipartFile file, Principal principal) throws IOException {
	    try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
	        Sheet rolesSheet = workbook.getSheet("Roles");

	        User user = userRepository.findByEmailId(principal.getName());

	        List<String> validationMessages = validateRolesSheet(rolesSheet);

	        if (!validationMessages.isEmpty()) {
	            throw new DataProcessingException(String.join(", ", validationMessages));
	        }
	        
	     // Specify an absolute path on your server where you have write permissions
	        //String uploadDirectory = "src/main/uploads/templates/"; // Replace with your actual directory path
	        String uploadDirectory = "/Resume-Builder-Backend/uploads" + File.separator;
	        String uploadedFileName = file.getOriginalFilename();
	        String targetFilePath = uploadDirectory + uploadedFileName;
	        File targetFile = new File(targetFilePath);

	        System.out.println("Upload Directory: " + uploadDirectory);
	        System.out.println("Uploaded File Name: " + uploadedFileName);
	        System.out.println("Target File Path: " + targetFilePath);

	        // Create the target directory if it doesn't exist
	        File directory = new File(uploadDirectory);
	        if (!directory.exists()) {
	            directory.mkdirs(); // Creates any missing directories in the path
	        }

	        file.transferTo(targetFile);
	        System.out.println("Upload Directory: " + uploadDirectory);
	        System.out.println("Uploaded File Name: " + uploadedFileName);
	        System.out.println("Target File Path: " + targetFilePath);

	        // Continue with saving the data
	        processRolesSheet(rolesSheet, user);
	    } catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> validateRolesSheet(Sheet sheet) {
	    List<String> validationMessages = new ArrayList<>();
	    Set<String> uniqueRoleNames = new HashSet<>();

	    for (Row row : sheet) {
	        if (row.getRowNum() == 0) {
	            // Skip the header row
	            continue;
	        }

	        for (Map.Entry<Integer, String> entry : rolesColumnMapping.entrySet()) {
	            Integer columnIndex = entry.getKey();
	            String fieldName = entry.getValue();
	            String cellValue = getStringValue(row.getCell(columnIndex));

	            if (fieldName.equals("role_name")) {
	                // Check for duplicate role names
	                if (uniqueRoleNames.contains(cellValue)) {
	                    validationMessages.add("Duplicate data entry for role name: " + cellValue);
	                } else {
	                    uniqueRoleNames.add(cellValue);
	                }

	                // Check for missing values
	                if (cellValue == null || cellValue.isEmpty()) {
	                    validationMessages.add("Data missing for role name");
	                }
	            }
	        }
	    }

	    return validationMessages;
	}

	private void processRolesSheet(Sheet sheet, User currentUser) {
		List<Roles> existingRoles = rolesRepository.findAll();
	    for (Row row : sheet) {
	        if (row.getRowNum() == 0) {
	            // Skip the header row
	            continue;
	        }

	        Roles roles = new Roles();
	        roles.setModified_by(currentUser.getFull_name());

	        for (Map.Entry<Integer, String> entry : rolesColumnMapping.entrySet()) {
	            Integer columnIndex = entry.getKey();
	            String fieldName = entry.getValue();
	            String cellValue = getStringValue(row.getCell(columnIndex));

	            setFieldValue(roles, fieldName, cellValue);
	        }
	        	
	        Roles existingRole = findRoleByName(existingRoles, roles.getRole_name());
	        	 if (existingRole == null) {
	                 // Role doesn't exist, so create a new one
	                 roles.setModified_by(currentUser.getFull_name());
	                 roles.setModified_on(LocalDateTime.now());
	                 rolesRepository.save(roles);
	             }else if (existingRole.is_deleted()) {
	               Roles newRole = new Roles();
                   newRole.setRole_name(roles.getRole_name());
                   newRole.setModified_by(currentUser.getFull_name());
                   newRole.setModified_on(LocalDateTime.now());
                   newRole.set_deleted(false);
                   rolesRepository.save(newRole);
	            	
	             }
	        	
	        }
	        //rolesRepository.save(roles);
	    }
	    
	    
	
	
	private Roles findRoleByName(List<Roles> rolesList, String roleName) {
	    for (Roles role : rolesList) {
	        if (role.getRole_name().equals(roleName) && role.is_deleted() == false ){
	            return role;
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

    private void setFieldValue(Roles roles, String fieldName, String cellValue) {
        try {
            Field field = roles.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (field.getType() == String.class) {
                field.set(roles, cellValue);
            } else if (field.getType() == Integer.class) {
                field.set(roles, Integer.parseInt(cellValue));
            }else if (field.getType() == LocalDateTime.class) {
                LocalDateTime localDateTime = LocalDateTime.parse(cellValue);
                field.set(roles, localDateTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
