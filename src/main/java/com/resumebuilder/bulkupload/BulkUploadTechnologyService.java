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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resumebuilder.DTO.RolesDto;
import com.resumebuilder.DTO.TechnologyDto;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.exception.DataProcessingException;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;
import com.resumebuilder.technology.TechnologyMaster;
import com.resumebuilder.technology.TechnologyMasterRepository;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;

/**
 * Service for bulk uploading technology data from an Excel file.
 */

@Service
public class BulkUploadTechnologyService {
	
	public static String fileSeparator = System.getProperty("file.separator");
	public static final String EXCEL_TEMPLATE_DIRECTORY = "upload"+fileSeparator+"template"+fileSeparator+"technology"; 
	 public static final Logger logger = LogManager.getLogger(BulkUploadRoleService.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private TechnologyMasterRepository technologyRepository;
	
	@Autowired
	private ActivityHistoryService activityHistoryService;
		
	/**
     * Process the uploaded Excel file containing technology data.
     *
     * @param file      The uploaded Excel file.
     * @param principal The Principal object representing the user.
     * @return A list of TechnologyDto objects with processed technology data.
     * @throws Exception If an error occurs during the processing.
     */

    @Transactional
    public List<TechnologyDto> processTechnologyExcelFile(MultipartFile file, Principal principal) throws Exception {
        try {
            // Validate the data in the Excel sheet
            List<TechnologyDto> technologyBulkUploadDtos;
            try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
                Sheet technologySheet = workbook.getSheet("Technologies");
                logger.info("Number of Rows: " + technologySheet.getPhysicalNumberOfRows());
                technologyBulkUploadDtos = validateTechnologyData(technologySheet);
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
            return processTechnologySheet(technologyBulkUploadDtos, user);
        } catch (IOException e) {
            // Handle the exception, you can log it and throw a custom exception or return an error response.
            logger.error("Error processing the Excel file: " + e.getMessage());
            e.printStackTrace();
            throw e;
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the Excel file.");
        }
		
    }   
    
    /**
     * Process the technology data from the Excel sheet and update the database.
     *
     * @param technologyBulkUploadDtos List of TechnologyDto objects from Excel.
     * @param currentUser              The current user performing the upload.
     * @return A list of TechnologyDto objects with processed technology data.
     * @throws Exception If an error occurs during the processing.
     */

    private List<TechnologyDto> processTechnologySheet(List<TechnologyDto> technologyBulkUploadDtos, User currentUser) throws Exception {
        List<TechnologyMaster> existingRoles = technologyRepository.findAll();
        List<TechnologyDto> allData = new ArrayList<>();

        for (TechnologyDto bulkUploadDto : technologyBulkUploadDtos) {
            if (bulkUploadDto.getTechnology_name() != null) {
                TechnologyMaster existingTechology = findByTechnologyName(existingRoles, bulkUploadDto.getTechnology_name());
                if (existingTechology != null) {
                    if (existingTechology.is_deleted()) {
                        createTechnology(existingTechology, bulkUploadDto, currentUser);
                    } else {
                        updateTechnology(existingTechology, bulkUploadDto, currentUser);
                    }
                } else {
                    if (bulkUploadDto.getRemark().isEmpty()) {
                        TechnologyMaster newTechnology = new TechnologyMaster();
                        createTechnology(newTechnology, bulkUploadDto, currentUser);
                    }
                }
            }

            allData.add(bulkUploadDto);
        }

        return allData;
    }

    /**
     * Find an existing technology by its name.
     *
     * @param techList       List of existing technologies.
     * @param technologyName The name of the technology to find.
     * @return The found TechnologyMaster object or null if not found.
     */
    
    private TechnologyMaster findByTechnologyName(List<TechnologyMaster> techList, String technology_name) {
        for (TechnologyMaster tech : techList) {
            if (tech.getTechnology_name() != null && tech.getTechnology_name().equals(technology_name) && !tech.is_deleted()) {
                return tech;
            }
        }
        return null;
    }

    /**
     * Check if all fields in the TechnologyDto are null.
     *
     * @param bulkExcelTechDto The TechnologyDto object to check.
     * @return True if all fields are null, otherwise false.
     */
    
    private boolean allFieldsAreNull(TechnologyDto bulkExcelTechDto) {
        return bulkExcelTechDto.getTechnology_name() == null;
    }
    
    /**
     * Create a new technology record in the database.
     *
     * @param newTechnology The new TechnologyMaster object to create.
     * @param bulkUploadDto The TechnologyDto object with data to populate.
     * @param currentUser   The user performing the operation.
     */

    private void createTechnology(TechnologyMaster newTechnology, TechnologyDto bulkUploadDto, User currentUser) {
    	newTechnology.setTechnology_name(bulkUploadDto.getTechnology_name());
    	newTechnology.setModified_by(currentUser.getUser_id());
    	newTechnology.setModified_on(LocalDateTime.now());
    	
    	 String activityType = "Bulk upload";
	     String description = "Bulk upload of technologies";
	     
	     activityHistoryService.addActivity(activityType, description, bulkUploadDto.getTechnology_name(), null, currentUser.getFull_name());
    	
        technologyRepository.save(newTechnology);
    }
    
    /**
     * Update an existing technology record in the database.
     *
     * @param existingTechnology The existing TechnologyMaster object to update.
     * @param bulkUploadDto      The TechnologyDto object with data to update.
     * @param currentUser        The user performing the operation.
     */

    private void updateTechnology(TechnologyMaster existingTechnology, TechnologyDto bulkUploadDto, User currentUser) {
    	existingTechnology.setTechnology_name(bulkUploadDto.getTechnology_name());
    	existingTechnology.setModified_by(currentUser.getUser_id());
    	existingTechnology.setModified_on(LocalDateTime.now());
    	
    		
    	
        technologyRepository.save(existingTechnology);
    }
    
//    private List<TechnologyDto> validateTechnologyData(Sheet sheet) {
//        Set<String> processedRoles = new HashSet<>();
//        List<String> missingDataMsg = new ArrayList<>();
//        List<TechnologyDto> bulkExcelTechnologyDtos = new ArrayList<>();
//
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                continue; // Skip the header row
//            }
//
//            String technology_name = getStringValue(row.getCell(1)); // Assuming role name is in column 1
//
//            if (technology_name == null) {
//                missingDataMsg.add("Data missing for technology name");
//            } else {
//                if (processedRoles.contains(technology_name)) {
//                    missingDataMsg.add("Duplicate data entry for technology name: " + technology_name);
//                } else {
//                    processedRoles.add(technology_name);
//                }
//            }
//
//            TechnologyDto bulkExcelTechDto = new TechnologyDto();
//            bulkExcelTechDto.setTechnology_name(technology_name);
//            bulkExcelTechDto.setRemark(missingDataMsg.stream().toList());
//            bulkExcelTechDto.setStatus(!allFieldsAreNull(bulkExcelTechDto));
//
//            bulkExcelTechnologyDtos.add(bulkExcelTechDto);
//
//            missingDataMsg.clear();
//        }
//
//        logger.info("Bulk Excel Employee Dto received {}", bulkExcelTechnologyDtos);
//        return bulkExcelTechnologyDtos;
//    }
    
    
    /**
     * Validate the data in the Excel sheet and return a list of TechnologyDto objects.
     *
     * @param sheet The Excel sheet to validate.
     * @return A list of TechnologyDto objects with processed data.
     */
    
    private List<TechnologyDto> validateTechnologyData(Sheet sheet) {
        Set<String> processedTechnology = new HashSet<>();
        List<String> missingDataMsg = new ArrayList<>();
        List<TechnologyDto> bulkExcelTechnologyDtos = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip the header row
            }

            String technology_name = getStringValue(row.getCell(1)); // Assuming role name is in column 1

            if (technology_name == null) {
                missingDataMsg.add("Data missing for role name");
            } else {
                if (processedTechnology.contains(technology_name)) {
                    missingDataMsg.add("Duplicate data entry for role name: " + technology_name);
                } else {
                	processedTechnology.add(technology_name);
                }
            }

            TechnologyDto bulkExcelTechDto = new TechnologyDto();
            bulkExcelTechDto.setTechnology_name(technology_name);
            bulkExcelTechDto.setRemark(missingDataMsg.stream().toList());
            bulkExcelTechDto.setStatus(missingDataMsg.isEmpty());
            
            bulkExcelTechnologyDtos.add(bulkExcelTechDto);
            missingDataMsg.clear();
        }

        logger.info("Bulk Excel Employee Dto received {}", bulkExcelTechnologyDtos);
        return bulkExcelTechnologyDtos;
    }

    /**
     * Get the string value of a cell in the Excel sheet.
     *
     * @param cell The Excel cell to extract the value from.
     * @return The string representation of the cell's value.
     */
    
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
