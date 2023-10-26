package com.resumebuilder.bulkupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.Principal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BulkUploadEmployeeService {
	
	public static final String EXCEL_TEMPLATE_DIRECTORY = "upload/template/employee"; 
	

		@Autowired
	    private UserRepository userRepository;
	    
	    @Autowired
	    private Map<Integer, String> employeeColumnMapping; // Inject the mapping for Employee
	    
	
	    @Transactional
	    public void processEmployeeExcelFile(MultipartFile file, Principal principal) {

	    	try {
				// Save the uploaded Excel file to the project path
	            String fileName = file.getOriginalFilename();
	            File destFile = new File(EXCEL_TEMPLATE_DIRECTORY, fileName);

	            // Ensure the target directory exists, if not, create it
	            File directory = destFile.getParentFile();
	            if (!directory.exists()) {
	                directory.mkdirs();
	            }
	         // Copy the file's input stream to the destination file
	            try (InputStream inputStream = file.getInputStream();
	                 FileOutputStream outputStream = new FileOutputStream(destFile)) {
	                byte[] buffer = new byte[1024];
	                int bytesRead;
	                while ((bytesRead = inputStream.read(buffer)) != -1) {
	                    outputStream.write(buffer, 0, bytesRead);
	                }
	            }
	    	
	    	
	        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
	            Sheet employeeSheet = workbook.getSheet("Employees");	         	
	            // Process and save data from employeeSheet
	            List<String> checkValid = validateEmployeeData(employeeSheet);
	            
	            User user = userRepository.findByEmailId(principal.getName());
	            
	            if (!checkValid.isEmpty()) {
	            	String errorMessage = String.join(", ", checkValid);
	            	System.err.println("Error Msg -"+errorMessage);
		            throw new DataProcessingException(errorMessage);
		        }
					processEmployeeSheet(employeeSheet, employeeColumnMapping, user);

	        } catch (java.io.IOException e) {
	            e.printStackTrace();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }	        
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
	                    // If the cell contains a date, convert it to a string in your desired format
	                    Date date = cell.getDateCellValue();
	                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // You can change the date format as needed
	                    return dateFormat.format(date);
	                } else {
	                    // If it's not a date, treat it as a numeric value
	                    return String.valueOf((int) cell.getNumericCellValue());
	                }
	            default:
	                return null; 
	        }
	    }

	    
	    private void processEmployeeSheet(Sheet sheet, Map<Integer, String> columnMapping, User currentUser) {
	    	List<User> existingUsers = userRepository.findAll();
	
	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) {
	                // Skip the header rowprocessEprocessEmployeeExcelFilemployeeExcelFile
	                continue;
	            }

	            String employeeId = getStringValue(row.getCell(1)); // Assuming EMP ID is in column 1
	            String fullName = getStringValue(row.getCell(2)); // Employee Full Name
	            String dateOfJoining = getStringValue(row.getCell(3)); // DoJ
	            String dateOfBirth = getStringValue(row.getCell(4)); // DoB
	            String currentRole = getStringValue(row.getCell(5)); // Current Role
	            String email = getStringValue(row.getCell(6)); // Email
	            
	            User user = new User();
	            user.setModified_by(currentUser.getFull_name());
	            user.setModified_on(LocalDateTime.now());
	
	            for (Map.Entry<Integer, String> entry : columnMapping.entrySet()) {
	                Integer columnIndex = entry.getKey();
	                String fieldName = entry.getValue();
	                String cellValue = getStringValue(row.getCell(columnIndex));
	
	                if ("employee_Id".equals(fieldName)) {
	                    employeeId = cellValue;
	                } else if ("email".equals(fieldName)) {
	                    email = cellValue;
	                }
	
	                setFieldValue(user, fieldName, cellValue);
	               
	            }
	
//	            // Check for duplicate email
//	            if (email != null && !email.isEmpty()) {
//	                User existingUser = userRepository.findByEmail_Id(email);
//	                if (existingUser != null) {
//	                    // Update existing user
//	                    copyFields(user, existingUser);
//	                    userRepository.save(existingUser);
//	                } else {
//	                    // User not found in the database, save as a new user
//	                    userRepository.save(user);
//	                }
//	            }
	            
	            
	            User existingUser = findUserByEmail(existingUsers, user.getEmail());
	            
	            if (existingUser == null) {
	                 user.setModified_by(currentUser.getFull_name());
	                 user.setModified_on(user.getModified_on());                 
	                 userRepository.save(user);
	             }else if (existingUser.is_deleted()) {
	               User newUser = new User();
	               newUser.setFull_name(user.getFull_name());
	               newUser.setDate_of_joining(user.getDate_of_joining());
	               newUser.setDate_of_birth(user.getDate_of_birth());
	               newUser.setCurrent_role(user.getCurrent_role());
	               newUser.setEmail(user.getEmail());
	               newUser.setGender(user.getGender());
	               newUser.setMobile_number(user.getMobile_number());
	               newUser.setLocation(user.getLocation());  
	               newUser.setModified_by(currentUser.getFull_name());
	               newUser.setModified_on(user.getModified_on());
                  userRepository.save(newUser);
	            	
	             }
	        }
	    }
	    
	    private User findUserByEmail(List<User> userList, String Email) {
		    for (User employee : userList) {
		        if (employee.getEmail() != null && employee.getEmail().equals(Email) && employee.is_deleted() == false ){		        
		            return employee;
		        }
		    }
		    return null;
		}
	    
	    private List<String> validateEmployeeData(Sheet sheet) {
	    	Set<String> processedEmployeeIds1 = new HashSet<>();
	        Set<String> processedEmails = new HashSet<>();
	        List<String> validationMessages = new ArrayList<>();

	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) {
	                // Skip the header row
	                continue;
	            }

	            String employeeId = getStringValue(row.getCell(1)); // Assuming EMP ID is in column 1
	            String fullName = getStringValue(row.getCell(2)); // Employee Full Name
	            String dateOfJoining = getStringValue(row.getCell(3)); // DoJ
	            String dateOfBirth = getStringValue(row.getCell(4)); // DoB
	            String currentRole = getStringValue(row.getCell(5)); // Current Role
	            String email = getStringValue(row.getCell(6)); // Email

	            List<String> missingDataMsg = new ArrayList<>();
	            List<String> duplicateDataMsg = new ArrayList<>();

	            if (employeeId == null || employeeId.isEmpty()) {
	                missingDataMsg.add("Data missing for EMP ID");
	            }
	            if (fullName == null || fullName.isEmpty()) {
	                missingDataMsg.add("Data missing for Employee Full Name");
	            }
	            if (dateOfJoining == null || dateOfJoining.isEmpty()) {
	                missingDataMsg.add("Data missing for DoJ");
	            }
	            if (dateOfBirth == null || dateOfBirth.isEmpty()) {
	                missingDataMsg.add("Data missing for DoB");
	            }
	            if (currentRole == null || currentRole.isEmpty()) {
	                missingDataMsg.add("Data missing for Current Role");
	            }
	            if (email == null || email.isEmpty()) {
	                missingDataMsg.add("Data missing for email");
	            }

	            if (processedEmployeeIds1.contains(employeeId)) {
	                duplicateDataMsg.add("Duplicate data entry for EMP ID: " + employeeId);
	            } else {
	                processedEmployeeIds1.add(employeeId);
	            }

	            if (processedEmails.contains(email)) {
	                duplicateDataMsg.add("Duplicate data entry for Email: " + email);
	            } else {
	                processedEmails.add(email);
	            }

	            validationMessages.addAll(missingDataMsg);
	            validationMessages.addAll(duplicateDataMsg);
	        }

	        return validationMessages;
	    }


	    
	    private void setFieldValue(Object object, String fieldName, String cellValue) {
	        try {
	            Field field = object.getClass().getDeclaredField(fieldName);
	            field.setAccessible(true);
	            if (field.getType().equals(String.class)) {
	                field.set(object, cellValue);
	            } else if (field.getType().equals(Integer.class)) {
	                field.set(object, Integer.parseInt(cellValue));
	            }else if (field.getType() == LocalDateTime.class) {
	                LocalDateTime localDateTime = LocalDateTime.parse(cellValue);
	                field.set(object, localDateTime);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
