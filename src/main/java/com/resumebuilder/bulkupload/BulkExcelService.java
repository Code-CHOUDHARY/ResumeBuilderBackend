package com.resumebuilder.bulkupload;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Principal;
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

import com.resumebuilder.DTO.UserDto;
import com.resumebuilder.exception.DataMissingException;
import com.resumebuilder.exception.DuplicateDataEntryException;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;
import com.resumebuilder.technology.TechnologyMaster;
import com.resumebuilder.technology.TechnologyMasterRepository;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class BulkExcelService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TechnologyMasterRepository technologyRepository;
    
    @Autowired
    private RolesRepository rolesRepository;
    
    @Autowired
    private Map<Integer, String> employeeColumnMapping; // Inject the mapping for Employee
    
    @Autowired
    private Map<Integer, String> technologyColumnMapping; // Inject the mapping for Technology
    
    @Autowired
    private Map<Integer, String> rolesColumnMapping; // Inject the mapping for Roles
    
//    //first main code
//    
//    public void processExcelFile(MultipartFile file) throws IOException {
//        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
//            Sheet employeeSheet = workbook.getSheet("Employees");
//            Sheet technologySheet = workbook.getSheet("Technologies");
//            Sheet rolesSheet = workbook.getSheet("Roles");
//
//            // Process and save data from employeeSheet
//            processEmployeeSheet(employeeSheet, employeeColumnMapping);
//
//            // Process and save data from technologySheet
//            processTechnologySheet(technologySheet);
//
//            // Process and save data from rolesSheet
//            processRolesSheet(rolesSheet);
//        }
//    }
//
//    
//    private String getStringValue(Cell cell) {
//        if (cell == null) {
//            return null;
//        }
//
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    // If the cell contains a date, convert it to a string in your desired format
//                    Date date = cell.getDateCellValue();
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // You can change the date format as needed
//                    return dateFormat.format(date);
//                } else {
//                    // If it's not a date, treat it as a numeric value
//                    return String.valueOf((int) cell.getNumericCellValue());
//                }
//            default:
//                return null; 
//        }
//    }
//
////    //first code
////    private void processEmployeeSheet(Sheet sheet, Map<Integer, String> columnMapping) {
////        Set<String> processedEmployeeIds = new HashSet<>();
////        Set<String> processedEmails = new HashSet<>();
////
////        for (Row row : sheet) {
////            if (row.getRowNum() == 0) {
////                // Skip the header row
////                continue;
////            }
////
////            User user = new User();
////            String employeeId = null;
////            String email = null;
////
////            for (Map.Entry<Integer, String> entry : columnMapping.entrySet()) {
////                Integer columnIndex = entry.getKey();
////                String fieldName = entry.getValue();
////                String cellValue = getStringValue(row.getCell(columnIndex));
////
////                if ("employee_Id".equals(fieldName)) {
////                    employeeId = cellValue;
////                } else if ("email".equals(fieldName)) {
////                    email = cellValue;
////                }
////
////                setFieldValue(user, fieldName, cellValue);
////            }
////
////         // Check for null values
////            if (employeeId == null || employeeId.isEmpty() || email == null || email.isEmpty()) {
////                throw new DataMissingException("Data missing for employee_Id or email");
////            }
////            
////            
////            // Check for duplicate employee_Id
////            if (employeeId != null && !employeeId.isEmpty()) {
////                User existingUser = userRepository.findByEmployee_Id(employeeId);
////                if (existingUser != null) {
////                	existingUser.setEmployee_Id(user.getEmployee_Id());
////                    existingUser.setFull_name(user.getFull_name());
////                    existingUser.setDate_of_joining(user.getDate_of_joining());
////                    existingUser.setDate_of_birth(user.getDate_of_birth());
////                    existingUser.setCurrent_role(user.getCurrent_role());
////                    existingUser.setEmail(user.getEmail());
////                    existingUser.setGender(user.getGender());
////                    existingUser.setMobile_number(user.getMobile_number());
////                    existingUser.setLocation(user.getLocation());
////                    
////                    userRepository.save(existingUser);
////                } else {
////                    // User not found in the database, save as new user
////                    userRepository.save(user);
////                }
////            }
////            
////
////            // Check for duplicate email
////            if (email != null && !email.isEmpty()) {
////                User existingUser = userRepository.findByEmail_Id(email);
////                if (existingUser != null) {
////                	existingUser.setEmployee_Id(user.getEmployee_Id());
////                    existingUser.setFull_name(user.getFull_name());
////                    existingUser.setDate_of_joining(user.getDate_of_joining());
////                    existingUser.setDate_of_birth(user.getDate_of_birth());
////                    existingUser.setCurrent_role(user.getCurrent_role());
////                    existingUser.setEmail(user.getEmail());
////                    existingUser.setGender(user.getGender());
////                    existingUser.setMobile_number(user.getMobile_number());
////                    existingUser.setLocation(user.getLocation());
////                    
////                    userRepository.save(existingUser);
////                } else {
////                    // User not found in the database, save as new user
////                    userRepository.save(user);
////                }
////            }
////
////            
////        }
////    }
//
//    //second code
//    private void processEmployeeSheet(Sheet sheet, Map<Integer, String> columnMapping) {
//        Set<String> processedEmployeeIds = new HashSet<>();
//        Set<String> processedEmails = new HashSet<>();
//
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                // Skip the header row
//                continue;
//            }
//
//            User user = new User();
//            String employeeId = null;
//            String email = null;
//
//            for (Map.Entry<Integer, String> entry : columnMapping.entrySet()) {
//                Integer columnIndex = entry.getKey();
//                String fieldName = entry.getValue();
//                String cellValue = getStringValue(row.getCell(columnIndex));
//
//                if ("employee_Id".equals(fieldName)) {
//                    employeeId = cellValue;
//                } else if ("email".equals(fieldName)) {
//                    email = cellValue;
//                }
//
//                setFieldValue(user, fieldName, cellValue);
//                
//                // Check for missing values
//                if (cellValue == null || cellValue.isEmpty()) {
//                    throw new DataMissingException("Data missing for " + fieldName);
//                }
//            }
//
//            // Check for duplicate employee_Id
//            if (employeeId != null && !employeeId.isEmpty()) {
//                User existingUser = userRepository.findByEmployee_Id(employeeId);
//                if (existingUser != null) {
//                    // Update existing user
//                    copyFields(user, existingUser);
//                    userRepository.save(existingUser);
//                } else {
//                    // User not found in the database, save as a new user
//                    userRepository.save(user);
//                }
//            }
//
//            // Check for duplicate email
//            if (email != null && !email.isEmpty()) {
//                User existingUser = userRepository.findByEmail_Id(email);
//                if (existingUser != null) {
//                    // Update existing user
//                    copyFields(user, existingUser);
//                    userRepository.save(existingUser);
//                } else {
//                    // User not found in the database, save as a new user
//                    userRepository.save(user);
//                }
//            }
//        }
//    }
//
//    private void copyFields(User source, User destination) {
//        destination.setEmployee_Id(source.getEmployee_Id());
//        destination.setFull_name(source.getFull_name());
//        destination.setDate_of_joining(source.getDate_of_joining());
//        destination.setDate_of_birth(source.getDate_of_birth());
//        destination.setCurrent_role(source.getCurrent_role());
//        destination.setEmail(source.getEmail());
//        destination.setGender(source.getGender());
//        destination.setMobile_number(source.getMobile_number());
//        destination.setLocation(source.getLocation());
//    }
//
//    
//    
//    private void setFieldValue(Object object, String fieldName, String cellValue) {
//        try {
//            Field field = object.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true);
//            if (field.getType().equals(String.class)) {
//                field.set(object, cellValue);
//            } else if (field.getType().equals(Integer.class)) {
//                field.set(object, Integer.parseInt(cellValue));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    private void processTechnologySheet(Sheet sheet) {
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                // Skip the header row
//                continue;
//            }
//
//            TechnologyMaster technology = new TechnologyMaster();
//            for (Map.Entry<Integer, String> entry : technologyColumnMapping.entrySet()) {
//                Integer columnIndex = entry.getKey();
//                String fieldName = entry.getValue();
//                String cellValue = getStringValue(row.getCell(columnIndex));
//                setFieldValue(technology, fieldName, cellValue);
//            }
//           
//
//            // Check for duplicate technology entry
//            String technologyName = technology.getTechnology_name();
//            
//         // Check for null values
//            if (technologyName == null || technologyName.isEmpty()) {
//                throw new DataMissingException("Data missing for technology");
//            }
//            TechnologyMaster existingTechnology = technologyRepository.findByTechnologyName(technologyName);
//            if (existingTechnology != null) {
//                existingTechnology.setTechnology_name(technology.getTechnology_name());
//                technologyRepository.save(existingTechnology);
//            } else {
//                technologyRepository.save(technology);
//            }
//        }
//    }
//    
//    private void processRolesSheet(Sheet sheet) {
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                // Skip the header row
//                continue;
//            }
//
//            Roles roles = new Roles();
//            for (Map.Entry<Integer, String> entry : rolesColumnMapping.entrySet()) {
//                Integer columnIndex = entry.getKey();
//                String fieldName = entry.getValue();
//                String cellValue = getStringValue(row.getCell(columnIndex));
//                setFieldValue(roles, fieldName, cellValue);
//            }
//
//            // Check for duplicate roles entry
//            String roleName = roles.getRole_name();
//         // Check for null values
//            if (roleName == null || roleName.isEmpty()) {
//                throw new DataMissingException("Data missing for role");
//            }
//            
//            Roles existingRoles = rolesRepository.findByRoleName(roleName);
//            if (existingRoles != null) {
//                existingRoles.setRole_name(roles.getRole_name());
//                rolesRepository.save(existingRoles);
//            } else {
//                rolesRepository.save(roles);
//            }
//        }
//    }
    
    
    //==============second main code==============
    
    
//    public void processExcelFile(MultipartFile file) throws IOException {
//        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
//            Sheet employeeSheet = workbook.getSheet("Employees");
//            Sheet technologySheet = workbook.getSheet("Technologies");
//            Sheet rolesSheet = workbook.getSheet("Roles");
//
//            List<String> missingFieldsEmployee = processEmployeeSheet(employeeSheet, employeeColumnMapping);
//            List<String> missingFieldsTechnology = processTechnologySheet(technologySheet);
//            List<String> missingFieldsRoles = processRolesSheet(rolesSheet);
//
//            //for data entry null
//            List<String> allMissingFields = new ArrayList<>();
//            allMissingFields.addAll(missingFieldsEmployee);
//            allMissingFields.addAll(missingFieldsTechnology);
//            allMissingFields.addAll(missingFieldsRoles);
////
////            if (!allMissingFields.isEmpty()) {
////                throw new DataMissingException("Data missing for the following fields: " + String.join(", ", allMissingFields));
////            }
//            
//
//            if (!allMissingFields.isEmpty()) {
//                throw new DataMissingException(String.join(", ", allMissingFields));
//            }
//            
//            //for duplicate data entry
//            List<String> duplicateDataEntry = new ArrayList<>();
//            duplicateDataEntry.addAll(missingFieldsEmployee);
//            duplicateDataEntry.addAll(missingFieldsTechnology);
//            duplicateDataEntry.addAll(missingFieldsRoles);
//            
//            if (duplicateDataEntry != null && !duplicateDataEntry.isEmpty()) {
//            	throw new DuplicateDataEntryException(String.join(", ", duplicateDataEntry));
//            }
//        }
//    }
//
//    private String getStringValue(Cell cell) {
//        if (cell == null) {
//            return null;
//        }
//
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    // If the cell contains a date, convert it to a string in your desired format
//                    Date date = cell.getDateCellValue();
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // You can change the date format as needed
//                    return dateFormat.format(date);
//                } else {
//                    // If it's not a date, treat it as a numeric value
//                    return String.valueOf((int) cell.getNumericCellValue());
//                }
//            default:
//                return null;
//        }
//    }
//
//    private List<String> processEmployeeSheet(Sheet sheet, Map<Integer, String> columnMapping) {
//        List<String> missingDataMessages = new ArrayList<>();
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                // Skip the header row
//                continue;
//            }
//
//            User user = new User();
//            String employeeId = null;
//            String email = null;
//
//            for (Map.Entry<Integer, String> entry : columnMapping.entrySet()) {
//                Integer columnIndex = entry.getKey();
//                String fieldName = entry.getValue();
//                String cellValue = getStringValue(row.getCell(columnIndex));
//
//                if ("employee_Id".equals(fieldName)) {
//                    employeeId = cellValue;
//                } else if ("email".equals(fieldName)) {
//                    email = cellValue;
//                }
//
//                setFieldValue(user, fieldName, cellValue);
//                
//                if (cellValue == null || cellValue.isEmpty()) {
//                    missingDataMessages.add("Data missing for " + fieldName);
////                }else {
////                	throw new DataMissingException("Data missing for " + fieldName);
//                }
//            }
//
////            // Check for duplicate employee_Id
////            if (employeeId != null && !employeeId.isEmpty()) {
////                User existingUser = userRepository.findByEmployee_Id(employeeId);
////                if (existingUser != null) {
////                    // Update existing user
////                    copyFieldsUser(user, existingUser);
////                    userRepository.save(existingUser);
////                } else {
////                    // User not found in the database, save as a new user
////                    userRepository.save(user);
////                }
////            }
//
//            // Check for duplicate email
//            if (email != null && !email.isEmpty()) {
//                User existingUser = userRepository.findByEmail_Id(email);
//                if (existingUser != null) {
//                    // Update existing user
//                    copyFieldsUser(user, existingUser);
//                    userRepository.save(existingUser);
//                } else {
//                    // User not found in the database, save as a new user
//                    userRepository.save(user);
//                }
//            }
//
//        }
//        return missingDataMessages;
//    }
//
//    private List<String> processTechnologySheet(Sheet sheet) {
//        List<String> missingDataMessages = new ArrayList<>();
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                // Skip the header row
//                continue;
//            }
//
//            TechnologyMaster technology = new TechnologyMaster();
//
//            for (Map.Entry<Integer, String> entry : technologyColumnMapping.entrySet()) {
//                Integer columnIndex = entry.getKey();
//                String fieldName = entry.getValue();
//                String cellValue = getStringValue(row.getCell(columnIndex));
//
//                setFieldValue(technology, fieldName, cellValue);
//
//                // Check for null values and add missing field names to the list
//                if (cellValue == null || cellValue.isEmpty()) {
//                    missingDataMessages.add("Data missing for " + fieldName);
//                	//throw new DataMissingException("Data missing for " + fieldName);
//                }
//            }
//
//            String technologyName = technology.getTechnology_name();
//
//            // Check for duplicate technology entry
//            if (technologyName != null && !technologyName.isEmpty()) {
//                TechnologyMaster existingTechnology = technologyRepository.findByTechnologyName(technologyName);
//                if (existingTechnology != null) {
//                    // Update existing technology
//                    copyFieldsTechnology(technology, existingTechnology);
//                    technologyRepository.save(existingTechnology);
//                } else {
//                    technologyRepository.save(technology);
//                }
//            }
//        }
//        return missingDataMessages;
//    }
//
//    private List<String> processRolesSheet(Sheet sheet) {
//        List<String> missingDataMessages = new ArrayList<>();
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                // Skip the header row
//                continue;
//            }
//
//            Roles roles = new Roles();
//
//            for (Map.Entry<Integer, String> entry : rolesColumnMapping.entrySet()) {
//                Integer columnIndex = entry.getKey();
//                String fieldName = entry.getValue();
//                String cellValue = getStringValue(row.getCell(columnIndex));
//
//                setFieldValue(roles, fieldName, cellValue);
//
//                // Check for null values and add missing field names to the list
//                if (cellValue == null || cellValue.isEmpty()) {
//                    missingDataMessages.add("Data missing for " + fieldName);
//                	//throw new DataMissingException("Data missing for " + fieldName);
//                	
//                }
//            }
//
//            String roleName = roles.getRole_name();
//
//            // Check for duplicate roles entry
//            if (roleName != null && !roleName.isEmpty()) {
//                Roles existingRoles = rolesRepository.findByRoleName(roleName);
//                if (existingRoles != null) {
//                    // Update existing role
//                	copyFieldsRole(roles, existingRoles);
//                    rolesRepository.save(existingRoles);
//                } else {
//                    rolesRepository.save(roles);
//                }
//            }
//        }
//        return missingDataMessages;
//    }
//
//    private void copyFieldsUser(User source, User destination) {
//        destination.setEmployee_Id(source.getEmployee_Id());
//        destination.setFull_name(source.getFull_name());
//        destination.setDate_of_joining(source.getDate_of_joining());
//        destination.setDate_of_birth(source.getDate_of_birth());
//        destination.setCurrent_role(source.getCurrent_role());
//        destination.setEmail(source.getEmail());
//        destination.setGender(source.getGender());
//        destination.setMobile_number(source.getMobile_number());
//        destination.setLocation(source.getLocation());
//        destination.setModified_on(source.getModified_on());
//    }
//    
//    private void copyFieldsRole(Roles role, Roles destination) {
//    	destination.setRole_name(role.getRole_name());
//    	destination.setModified_on(role.getModified_on());
//    }
//    
//    private void copyFieldsTechnology(TechnologyMaster technology, TechnologyMaster destination) {
//    	destination.setTechnology_name(technology.getTechnology_name());
//    	destination.setModified_on(technology.getModified_on());
//    }
//
//    private void setFieldValue(Object object, String fieldName, String cellValue) {
//        try {
//            Field field = object.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true);
//            if (field.getType().equals(String.class)) {
//                field.set(object, cellValue);
//            } else if (field.getType().equals(Integer.class)) {
//                field.set(object, Integer.parseInt(cellValue));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    //==================3rd-main code =======================
    
    public void processExcelFile(MultipartFile file, Principal principal) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet employeeSheet = workbook.getSheet("Employees");
            Sheet technologySheet = workbook.getSheet("Technologies");
            Sheet rolesSheet = workbook.getSheet("Roles");

            User user = userRepository.findByEmailId(principal.getName());
            
            List<String> missingFieldsEmployee = processEmployeeSheet(employeeSheet, employeeColumnMapping, user);
            List<String> missingFieldsTechnology = processTechnologySheet(technologySheet, user);
            List<String> missingFieldsRoles = processRolesSheet(rolesSheet, user);

            //for data entry null
            List<String> allMissingFields = new ArrayList<>();
            allMissingFields.addAll(missingFieldsEmployee);
            allMissingFields.addAll(missingFieldsTechnology);
            allMissingFields.addAll(missingFieldsRoles);
//
//            if (!allMissingFields.isEmpty()) {
//                throw new DataMissingException("Data missing for the following fields: " + String.join(", ", allMissingFields));
//            }
            

            if (!allMissingFields.isEmpty()) {
                throw new DataMissingException(String.join(", ", allMissingFields));
            }
            
            //for duplicate data entry
            List<String> duplicateDataEntry = new ArrayList<>();
            duplicateDataEntry.addAll(missingFieldsEmployee);
            duplicateDataEntry.addAll(missingFieldsTechnology);
            duplicateDataEntry.addAll(missingFieldsRoles);
            
            if (duplicateDataEntry != null && !duplicateDataEntry.isEmpty()) {
            	throw new DuplicateDataEntryException(String.join(", ", duplicateDataEntry));
            }
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

    //employee
    private List<String> processEmployeeSheet(Sheet sheet, Map<Integer, String> columnMapping, User currentUser) {
        List<String> missingDataMessages = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Skip the header row
                continue;
            }

            User user = new User();
            String employeeId = null;
            String email = null;

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

                if (cellValue == null || cellValue.isEmpty()) {
                    missingDataMessages.add("Data missing for " + fieldName);
                }
            }

            // Check for duplicate employee_Id
            if (employeeId != null && !employeeId.isEmpty()) {
                User existingUser = userRepository.findByEmployee_Id(employeeId);
                if (existingUser != null) {
                    // Check if the existing user has been soft deleted
                    if (existingUser.is_deleted()) {
                        // Restore the soft-deleted user
                        existingUser.set_deleted(false);
                        existingUser.setModified_by(currentUser.getFull_name());
                        copyFieldsUser(user, existingUser, currentUser);
                        userRepository.save(existingUser);
                    } else {
                        copyFieldsUser(user, existingUser, currentUser);
                        userRepository.save(existingUser);
                    }
                } else {
                    // User not found in the database, save as a new user
                    userRepository.save(user);
                }
            }

            // Check for duplicate email
            if (email != null && !email.isEmpty()) {
                User existingUser = userRepository.findByEmail_Id(email);
                if (existingUser != null) {
                    // Update existing user
                    copyFieldsUser(user, existingUser, currentUser);
                    existingUser.setModified_by(currentUser.getFull_name());
                    userRepository.save(existingUser);
                } else {
                    // User not found in the database, save as a new user
                    userRepository.save(user);
                }
            }
        }
        return missingDataMessages;
    }

    //technology
    private List<String> processTechnologySheet(Sheet sheet, User currentUser) {
        List<String> missingDataMessages = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Skip the header row
                continue;
            }

            TechnologyMaster technology = new TechnologyMaster();

            for (Map.Entry<Integer, String> entry : technologyColumnMapping.entrySet()) {
                Integer columnIndex = entry.getKey();
                String fieldName = entry.getValue();
                String cellValue = getStringValue(row.getCell(columnIndex));

                setFieldValue(technology, fieldName, cellValue);

                if (cellValue == null || cellValue.isEmpty()) {
                    missingDataMessages.add("Data missing for " + fieldName);
                }
            }

            String technologyName = technology.getTechnology_name();

            // Check for duplicate technology entry
            if (technologyName != null && !technologyName.isEmpty()) {
                TechnologyMaster existingTechnology = technologyRepository.findByTechnologyName(technologyName);
                if (existingTechnology != null) {
                    // Check if the existing technology has been soft deleted
                    if (existingTechnology.is_deleted()) {
                        // Restore the soft-deleted technology
                        existingTechnology.set_deleted(false);
                        copyFieldsTechnology(technology, existingTechnology, currentUser);
                        technologyRepository.save(existingTechnology);
                    } else {
                        // Update existing technology
                        copyFieldsTechnology(technology, existingTechnology, currentUser);
                        technologyRepository.save(existingTechnology);
                    }
                } else {
                    technologyRepository.save(technology);
                }
            }
        }
        return missingDataMessages;
    }

    private List<String> processRolesSheet(Sheet sheet, User currentUser) {
        List<String> missingDataMessages = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Skip the header row
                continue;
            }

            Roles roles = new Roles();

            for (Map.Entry<Integer, String> entry : rolesColumnMapping.entrySet()) {
                Integer columnIndex = entry.getKey();
                String fieldName = entry.getValue();
                String cellValue = getStringValue(row.getCell(columnIndex));

                setFieldValue(roles, fieldName, cellValue);

                if (cellValue == null || cellValue.isEmpty()) {
                    missingDataMessages.add("Data missing for " + fieldName);
                }
            }

            String roleName = roles.getRole_name();

            // Check for duplicate roles entry
            if (roleName != null && !roleName.isEmpty()) {
                Roles existingRoles = rolesRepository.findByRoleName(roleName);
                if (existingRoles != null) {
                    // Check if the existing role has been soft deleted
                    if (existingRoles.is_deleted()) {
                        Roles newRole = new Roles();
                        newRole.setRole_name(roles.getRole_name());
                        newRole.setModified_by(currentUser.getFull_name());
                        rolesRepository.save(newRole);
                    } else {
                        // Update existing role
//                        copyFieldsRole(roles, existingRoles, currentUser);
//                        rolesRepository.save(existingRoles);
                    	throw new DuplicateDataEntryException("Duplicate data entry");
                    }
                } else {
                    rolesRepository.save(roles);
                }
            }
        }
        return missingDataMessages;
    }

    private void copyFieldsUser(User source, User destination, User currentUser) {
        destination.setEmployee_Id(source.getEmployee_Id());
        destination.setFull_name(source.getFull_name());
        destination.setDate_of_joining(source.getDate_of_joining());
        destination.setDate_of_birth(source.getDate_of_birth());
        destination.setCurrent_role(source.getCurrent_role());
        destination.setEmail(source.getEmail());
        destination.setGender(source.getGender());
        destination.setMobile_number(source.getMobile_number());
        destination.setLocation(source.getLocation());
        destination.setModified_by(currentUser.getFull_name());
        destination.setModified_on(source.getModified_on());
    }
    
    private void copyFieldsRole(Roles role, Roles destination, User currentUser) {
    	destination.setRole_name(role.getRole_name());
    	destination.setModified_on(role.getModified_on());
    	destination.setModified_by(currentUser.getFull_name());
    }
    
    private void copyFieldsTechnology(TechnologyMaster technology, TechnologyMaster destination, User currentUser) {
    	destination.setTechnology_name(technology.getTechnology_name());
    	destination.setModified_on(technology.getModified_on());
    	destination.setModified_by(currentUser.getFull_name());
    }

    private void setFieldValue(Object object, String fieldName, String cellValue) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (field.getType().equals(String.class)) {
                field.set(object, cellValue);
            } else if (field.getType().equals(Integer.class)) {
                field.set(object, Integer.parseInt(cellValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
       
    
}

