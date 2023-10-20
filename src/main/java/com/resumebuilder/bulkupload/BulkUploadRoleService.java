package com.resumebuilder.bulkupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

import org.apache.catalina.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resumebuilder.DTO.RolesDto;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;

@Service
public class BulkUploadRoleService {
	
	 public static final Logger logger = LogManager.getLogger(BulkUploadRoleService.class);

    public static final String EXCEL_TEMPLATE_DIRECTORY = "upload/template/role";

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RolesRepository rolesRepository;

        @Transactional
        public List<RolesDto> processRoleExcelFile(MultipartFile file, Principal principal) throws Exception {
            try {
                // Validate the data in the Excel sheet
                List<RolesDto> roleBulkUploadDtos;
                try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
                    Sheet roleSheet = workbook.getSheet("Roles");
                    logger.info("Number of Rows: " + roleSheet.getPhysicalNumberOfRows());
                    roleBulkUploadDtos = validateRoleData(roleSheet);
                }

                // Save the uploaded Excel file to the project path
                String fileName = file.getOriginalFilename();
                File destFile = new File(EXCEL_TEMPLATE_DIRECTORY, fileName);
                File directory = destFile.getParentFile();
                
                if (!directory.exists() && !directory.mkdirs()) {
                    throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
                }

                try (InputStream inputStream = file.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(destFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                User user = userRepository.findByEmailId(principal.getName());
                return processRoleSheet(roleBulkUploadDtos, user);
            } catch (IOException e) {
                // Handle the exception, you can log it and throw a custom exception or return an error response.
                logger.error("Error processing the Excel file: " + e.getMessage());
                e.printStackTrace();
                throw e;
                //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the Excel file.");
            }
			
        }   

        private List<RolesDto> processRoleSheet(List<RolesDto> roleBulkUploadDtos, User currentUser) throws Exception {
            List<Roles> existingRoles = rolesRepository.findAll();
            List<RolesDto> allData = new ArrayList<>();

            for (RolesDto bulkUploadDto : roleBulkUploadDtos) {
                if (bulkUploadDto.getRole_name() != null) {
                    Roles existingRole = findByRoleName(existingRoles, bulkUploadDto.getRole_name());
                    if (existingRole != null) {
                        if (existingRole.is_deleted()) {
                            createRole(existingRole, bulkUploadDto, currentUser);
                        } else {
                            updateRole(existingRole, bulkUploadDto, currentUser);
                        }
                    } else {
                        if (bulkUploadDto.getRemark().isEmpty()) {
                            Roles newRole = new Roles();
                            createRole(newRole, bulkUploadDto, currentUser);
                        }
                    }
                }

                allData.add(bulkUploadDto);
            }

            return allData;
        }


        private Roles findByRoleName(List<Roles> roleList, String role_name) {
            for (Roles role : roleList) {
                if (role.getRole_name() != null && role.getRole_name().equals(role_name) && !role.is_deleted()) {
                    return role;
                }
            }
            return null;
        }

        private boolean allFieldsAreNull(RolesDto dto) {
            return dto.getRole_name() == null;
        }

        private void createRole(Roles newRole, RolesDto bulkUploadDto, User currentUser) {
            newRole.setRole_name(bulkUploadDto.getRole_name());
            newRole.setModified_by(currentUser.getFull_name());
            newRole.setModified_on(LocalDateTime.now());
            rolesRepository.save(newRole);
        }

        private void updateRole(Roles existingRole, RolesDto bulkUploadDto, User currentUser) {
            existingRole.setRole_name(bulkUploadDto.getRole_name());
            existingRole.setModified_by(currentUser.getFull_name());
            existingRole.setModified_on(LocalDateTime.now());
            rolesRepository.save(existingRole);
        }
        
        private List<RolesDto> validateRoleData(Sheet sheet) {
            Set<String> processedRoles = new HashSet<>();
            List<String> missingDataMsg = new ArrayList<>();
            List<RolesDto> bulkExcelRolesDtos = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip the header row
                }

                String role_name = getStringValue(row.getCell(1)); // Assuming role name is in column 1

                if (role_name == null) {
                    missingDataMsg.add("Data missing for role name");
                } else {
                    if (processedRoles.contains(role_name)) {
                        missingDataMsg.add("Duplicate data entry for role name: " + role_name);
                    } else {
                        processedRoles.add(role_name);
                    }
                }

                RolesDto bulkExcelRoleDto = new RolesDto();
                bulkExcelRoleDto.setRole_name(role_name);
                bulkExcelRoleDto.setRemark(missingDataMsg.stream().toList());
                bulkExcelRoleDto.setStatus(!allFieldsAreNull(bulkExcelRoleDto));

                bulkExcelRolesDtos.add(bulkExcelRoleDto);

                missingDataMsg.clear();
            }

            logger.info("Bulk Excel Employee Dto received {}", bulkExcelRolesDtos);
            return bulkExcelRolesDtos;
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
    
}
