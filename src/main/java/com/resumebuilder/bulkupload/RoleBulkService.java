package com.resumebuilder.bulkupload;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Principal;
import java.text.SimpleDateFormat;

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
import com.resumebuilder.exception.DuplicateDataEntryException;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class RoleBulkService {

    @Autowired
    private RolesRepository rolesRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Map<Integer, String> rolesColumnMapping; // Inject the mapping for Roles

    public void processRoleExcelFile(MultipartFile file, Principal principal) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet rolesSheet = workbook.getSheet("Roles");

            User user = userRepository.findByEmailId(principal.getName());
            
            List<String> missingFieldsRoles = processRolesSheet(rolesSheet, user);

            //for data entry null
            List<String> allMissingFields = new ArrayList<>();
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
            duplicateDataEntry.addAll(missingFieldsRoles);
            
            if (duplicateDataEntry != null && !duplicateDataEntry.isEmpty()) {
            	throw new DuplicateDataEntryException(String.join(", ", duplicateDataEntry));
            }
        }
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
                    missingDataMessages.add("Data missing");
                }
                
            }

            String roleName = roles.getRole_name();
            
            if(roleName == null) {
            	throw new DataMissingException("Data missing");
            }

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
                       copyFieldsRole(roles, existingRoles, currentUser);
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
    
    private void copyFieldsRole(Roles role, Roles destination, User currentUser) {
    	destination.setRole_name(role.getRole_name());
    	destination.setModified_on(role.getModified_on());
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
}