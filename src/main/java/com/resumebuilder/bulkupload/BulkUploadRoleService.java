package com.resumebuilder.bulkupload;

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

import com.resumebuilder.exception.DataMissingException;
import com.resumebuilder.exception.DataProcessingException;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class BulkUploadRoleService {
	
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

	        // Continue with saving the data
	        processRolesSheet(rolesSheet, user);
	    } catch (java.io.IOException e) {
			// TODO Auto-generated catch block
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

	        // Check if the role already exists in the database
	        //List<Roles> roleList = rolesRepository.findByRolesName(roles.getRole_name());

	       // for(Roles existingRole : roleList) {   
	        	
//	        	if (existingRole.is_deleted()==true) {
//                    Roles newRole = new Roles();
//                    newRole.setRole_name(roles.getRole_name());
//                    newRole.setModified_by(currentUser.getFull_name());
//                    newRole.set_deleted(false);
//                    rolesRepository.save(newRole);
//                } else if (existingRole.is_deleted()==false) {
//                	rolesRepository.save(roles);
//                }
	        	
	        Roles existingRole = findRoleByName(existingRoles, roles.getRole_name());
	        	 if (existingRole == null) {
	                 // Role doesn't exist, so create a new one
	                 roles.setModified_by(currentUser.getFull_name());
	                 rolesRepository.save(roles);
	             }else if (existingRole.is_deleted()) {
	            	 Roles newRole = new Roles();
                   newRole.setRole_name(roles.getRole_name());
                   newRole.setModified_by(currentUser.getFull_name());
                   newRole.set_deleted(false);
                   rolesRepository.save(newRole);
	            	 
	                 // Only update if the existing role is deleted
//	                 existingRole.setModified_by(currentUser.getFull_name());
//	                 existingRole.set_deleted(false);
//	                 rolesRepository.save(existingRole);
	             }
	        	
	        }
	        //rolesRepository.save(roles);
	    }
	    
	    
	
	
	private Roles findRoleByName(List<Roles> rolesList, String roleName) {
	    for (Roles role : rolesList) {
	        if (role.getRole_name().equals(roleName)) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
