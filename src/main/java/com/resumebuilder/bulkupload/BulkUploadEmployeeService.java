package com.resumebuilder.bulkupload;

import com.resumebuilder.DTO.EmployeeBulkUploadDto;
import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;
import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;
import com.resumebuilder.user.UserRoleRepository;
import com.resumebuilder.user.UserRolesMapping;
import com.resumebuilder.user.UserRolesMappingRepository;
import com.resumebuilder.user.UserToJsonConverter;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for handling bulk upload of employee data from Excel files.
 */

@Service
public class BulkUploadEmployeeService {

    public static final Logger logger = LogManager.getLogger(BulkUploadEmployeeService.class);
    
    public static String fileSeparator = System.getProperty("file.separator");

    public static final String EXCEL_TEMPLATE_DIRECTORY = "upload"+fileSeparator+"template"+fileSeparator+"employee";

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
	private UserRolesMappingRepository userRolesMappingRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRoleRepository roleRepository;
    
    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ActivityHistoryService activityHistoryService;
    
    
    /**
     * Processes an Excel file containing employee data.
     *
     * @param file      The Excel file to process.
     * @param principal The principal user initiating the upload.
     * @return A list of EmployeeBulkUploadDto objects containing processed data.
     * @throws Exception If there is an error during processing.
     */
    

    @Transactional
    public List<EmployeeBulkUploadDto> processEmployeeExcelFile(MultipartFile file, Principal principal) throws Exception {
        try {
            // Validate the data in the Excel sheet
            List<EmployeeBulkUploadDto> employeeBulkUploadDtos;
            try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
                Sheet employeeSheet = workbook.getSheet("Employees");

                logger.info("Number of Rows: " + employeeSheet.getPhysicalNumberOfRows());

                employeeBulkUploadDtos = validateEmployeeData(employeeSheet);
            }

            // Save the uploaded Excel file to the project path
            String fileName = file.getOriginalFilename();
            File destFile = new File(EXCEL_TEMPLATE_DIRECTORY, fileName);

            File directory = destFile.getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
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

            // Process and save data from employeeSheet
            User user = userRepository.findByEmailId(principal.getName());
            //return employeeBulkUploadDtos;
            return processEmployeeSheet(employeeBulkUploadDtos, user,principal);
        } catch (IOException e) {
            e.printStackTrace(); // You should consider proper error handling (e.g., logging or throwing a custom exception)
            throw e; // Rethrow the exception for error reporting or handling at a higher level.
        }
    }   

//    private List<EmployeeBulkUploadDto> processEmployeeSheet(List<EmployeeBulkUploadDto> employeeBulkUploadDto, User currentUser) throws Exception {
//        List<User> existingUsers = userRepository.findAll();
//        List<EmployeeBulkUploadDto> notStoredData = new ArrayList<>();
//        List<EmployeeBulkUploadDto> storedData = new ArrayList<>();
//
//        logger.info("Found List of users {}", employeeBulkUploadDto);
//
//        for (EmployeeBulkUploadDto bulkUploadDto : employeeBulkUploadDto) {
//          
//            if (bulkUploadDto.getEmployeeId() != null &&
//                    bulkUploadDto.getFullName() != null &&
//                    bulkUploadDto.getDateOfJoining() != null &&
//                    bulkUploadDto.getDateOfBirth() != null &&
//                    bulkUploadDto.getCurrentRole() != null &&
//                    bulkUploadDto.getEmail() != null &&
//                    bulkUploadDto.getGender() != null &&
//                    bulkUploadDto.getMobile_number() != null &&
//                    bulkUploadDto.getLocation() != null) {
//                User existingUser = findUserByEmail(existingUsers, bulkUploadDto.getEmail());
//
//                // Generate a random password
//                String generatedPassword = generateRandomPassword();
//                String encodedPassword = passwordEncoder.encode(generatedPassword);
//
//                if (existingUser != null) {
//                    if (existingUser.is_deleted()) {
//                        // User exists but is marked as deleted, so create a new user
//                        User user = createUser(existingUser, bulkUploadDto, generatedPassword, encodedPassword, currentUser);
//                        if (bulkUploadDto.getRemark().isEmpty()) {
//                            bulkUploadDto.setStatus(false);
//                            storedData.add(bulkUploadDto);
//                        }
//                    
//                    } else {
//                        // User exists, update the existing user
//                       User user = updateUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
//                       if (bulkUploadDto.getRemark().isEmpty()) {
//                           bulkUploadDto.setStatus(false);
//                           storedData.add(bulkUploadDto);
//                       }
//                    }
//                } else {
//                    // User doesn't exist, create a new user
//                    if (bulkUploadDto.getRemark().isEmpty()) {
//                        User newUser = new User();
//                        createUser(newUser, bulkUploadDto,generatedPassword, encodedPassword, currentUser);
//                    } else {
//                        bulkUploadDto.setStatus(true);
//                        notStoredData.add(bulkUploadDto);
//                    }
//                }
//
////                if (bulkUploadDto.getRemark().isEmpty()) {
////                    bulkUploadDto.setStatus(false);
////                    storedData.add(bulkUploadDto);
////                }
//            } else {
//                bulkUploadDto.setStatus(true);
//                notStoredData.add(bulkUploadDto);
//            }
//
//        }
//
//        // Return both the stored and not stored data
//        List<EmployeeBulkUploadDto> allData = new ArrayList<>();
//        allData.addAll(storedData);
//        allData.addAll(notStoredData);
//
//        return allData;
//    }
    
    /**
     * Processes the data from the employee sheet.
     *
     * @param employeeBulkUploadDtos A list of EmployeeBulkUploadDto objects containing technology data.
     * @param currentUser        The user initiating the update.
     * @return A list of processed EmployeeBulkUploadDto objects.
     * @throws Exception If there is an error during processing.
     */
    
    
//    private List<EmployeeBulkUploadDto> processEmployeeSheet(List<EmployeeBulkUploadDto> employeeBulkUploadDto, User currentUser) throws Exception {
//        List<User> existingUsers = userRepository.findAll();
//        List<EmployeeBulkUploadDto> notStoredData = new ArrayList<>();
//        List<EmployeeBulkUploadDto> storedData = new ArrayList<>();
//
//        logger.info("Found List of users {}", employeeBulkUploadDto);
//
//        for (EmployeeBulkUploadDto bulkUploadDto : employeeBulkUploadDto) {
//
//            if (bulkUploadDto.getEmployee_id() != null &&
//                    bulkUploadDto.getFullName() != null &&
//                    bulkUploadDto.getDateOfJoining() != null &&
//                    bulkUploadDto.getDateOfBirth() != null &&
//                    bulkUploadDto.getCurrentRole() != null &&
//                    bulkUploadDto.getEmail_id() != null &&
//                    bulkUploadDto.getGender() != null &&
//                    bulkUploadDto.getMobile_number() != null &&
//                    bulkUploadDto.getLocation() != null) {
//                User existingUser = findUserByEmailAndEmployeeId(existingUsers, bulkUploadDto.getEmail_id(), bulkUploadDto.getEmployee_id());
//
//                // Generate a random password
//                String generatedPassword = generateRandomPassword();
//                String encodedPassword = passwordEncoder.encode(generatedPassword);
//
//                if (bulkUploadDto.getRemark().isEmpty()) {
//                	
//                	List<Roles> allRoles = rolesRepository.findAll();
//                	Roles currentRole = allRoles.stream()
//                            .filter(role -> role.getRole_name().equals(bulkUploadDto.getCurrentRole()))
//                            .findFirst()
//                            .orElse(null);
//                	if (currentRole != null) {
//                		
//                    if (existingUser != null) {
//                        if (existingUser.is_deleted()) {
//                            // User exists but is marked as deleted, so create a new user
//                            createUser(existingUser, bulkUploadDto, generatedPassword, encodedPassword, currentUser);
//                            storedData.add(bulkUploadDto);
//                        } else {
//                            // User exists, update the existing user
////                            updateUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
////                            storedData.add(bulkUploadDto);
//                        	
//                        	// User exists, update the existing user
//                            if(Objects.equals(existingUser.getEmployee_Id(), bulkUploadDto.getEmployee_id())) {
//                                updateUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
//                                storedData.add(bulkUploadDto);
//                            } else {
//                                bulkUploadDto.setStatus(true);
//                                bulkUploadDto.setRemark(List.of("Employee id or email id is already exists"));
//                                notStoredData.add(bulkUploadDto);
//                            }
//                        }
//                    } else {
//                        // User doesn't exist, create a new user
////                        if (bulkUploadDto.getRemark().isEmpty()) {
////                            User newUser = new User();
////                            createUser(newUser, bulkUploadDto, generatedPassword, encodedPassword, currentUser);
////                            bulkUploadDto.setStatus(false);
////                            storedData.add(bulkUploadDto);
//                    	
//                    	 // User doesn't exist, create a new user
//                        if (bulkUploadDto.getRemark().isEmpty()) {
//                        	boolean existsByEmail = userRepository.existsByEmail(bulkUploadDto.getEmail_id());
//                            boolean existsByEmployeeId = userRepository.existsByEmployeeId(bulkUploadDto.getEmployee_id());
//                            boolean existBySoftDelete = userRepository.existsByEmployeeIdAndNotDeleted(bulkUploadDto.getEmployee_id());
//                            
//                            if (!existsByEmail && !existsByEmployeeId && !existBySoftDelete) {
//                                User newUser = new User();
//                                createUser(newUser, bulkUploadDto,generatedPassword, encodedPassword, currentUser);
//                                bulkUploadDto.setStatus(false);
//                                storedData.add(bulkUploadDto);
//                            } else {
//                                bulkUploadDto.setStatus(true);
//                                bulkUploadDto.setRemark(List.of("Employee id or email id is already exists"));
//                                notStoredData.add(bulkUploadDto);
//                            }
//                        } else {
//                            bulkUploadDto.setStatus(true);
//                            notStoredData.add(bulkUploadDto);
//                        }
//                    }
//                } else {
//                    bulkUploadDto.setStatus(true);
//                    bulkUploadDto.setRemark(List.of("Current role does not exist."));
//                    notStoredData.add(bulkUploadDto);
//                }
//            } else {
//                bulkUploadDto.setStatus(true);
//               
//                notStoredData.add(bulkUploadDto);
//            }
//            }
//        }
//
//        // Return both the stored and not stored data
//        List<EmployeeBulkUploadDto> allData = new ArrayList<>();
//        allData.addAll(storedData);
//        allData.addAll(notStoredData);
//
//        return allData;
//    }
    
    private List<EmployeeBulkUploadDto> processEmployeeSheet(List<EmployeeBulkUploadDto> employeeBulkUploadDto, User currentUser, Principal principal) throws Exception {
        List<User> existingUsers = userRepository.findAll();
        List<EmployeeBulkUploadDto> notStoredData = new ArrayList<>();
        List<EmployeeBulkUploadDto> storedData = new ArrayList<>();

        for (EmployeeBulkUploadDto bulkUploadDto : employeeBulkUploadDto) {
            if (bulkUploadDto.getEmployee_id() != null &&
                    bulkUploadDto.getFullName() != null &&
                    bulkUploadDto.getDateOfJoining() != null &&
                    bulkUploadDto.getDateOfBirth() != null &&
                    bulkUploadDto.getCurrentRole() != null &&
                    bulkUploadDto.getEmail_id() != null &&
                    bulkUploadDto.getGender() != null &&
//                    bulkUploadDto.getMobile_number() != null &&
//                    bulkUploadDto.getLocation() != null &&
                    bulkUploadDto.getAppRoleId() != null) {

                User existingUser = findUserByEmailAndEmployeeId(existingUsers, bulkUploadDto.getEmail_id(), bulkUploadDto.getEmployee_id());

                // Generate a random password
                String generatedPassword = generateRandomPassword();
                String encodedPassword = passwordEncoder.encode(generatedPassword);

                if (bulkUploadDto.getRemark().isEmpty()) {
                    List<Roles> allRoles = rolesRepository.findAll();
//                    Roles currentRole = allRoles.stream()
//                            .filter(role -> role.getRole_name().equals(bulkUploadDto.getCurrentRole()))
//                            .findFirst()
//                            .orElse(null);
                    Roles currentRole = rolesRepository.findByRoleName(bulkUploadDto.getCurrentRole());
                    logger.info("Employee current role --- "+currentRole);
                    if (currentRole != null) {
                        if (existingUser != null) {
                            // User exists, update the existing user
                            if (Objects.equals(existingUser.getEmployee_Id(), bulkUploadDto.getEmployee_id())) {
                                updateUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
                                storedData.add(bulkUploadDto);
                            } else {
                                // Existing user is soft-deleted, mark as active and update details
                                existingUser.set_deleted(false);
                                updateUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
                                storedData.add(bulkUploadDto);
                            }
                        } else {
                            // User doesn't exist, create a new user
                            boolean existsByEmail = userRepository.existsByEmail(bulkUploadDto.getEmail_id());
                            boolean existsByEmployeeId = userRepository.existsByEmployeeId(bulkUploadDto.getEmployee_id());
                            boolean existBySoftDelete = userRepository.existsByEmployeeIdAndNotDeleted(bulkUploadDto.getEmployee_id());
                            
                         // Check if there is a soft-deleted user with the same email ID and employee ID
                            if (!existsByEmail && !existsByEmployeeId) {                                                           

                                if (!existBySoftDelete) {
                                    // Create a new user
                                    User newUser = new User();
                                    createUser(newUser, bulkUploadDto, generatedPassword, encodedPassword, currentUser,principal);
                                    bulkUploadDto.setStatus(false);
                                    storedData.add(bulkUploadDto);
                                } else {
                                    // Soft-deleted user found, create a new user entry
                                    User newUser = new User();
                                    createUser(newUser, bulkUploadDto, generatedPassword, encodedPassword, currentUser,principal);
                                    bulkUploadDto.setStatus(false);
                                    storedData.add(bulkUploadDto);
                                }
                            } else {
                                bulkUploadDto.setStatus(true);
                                bulkUploadDto.setRemark(List.of("Employee ID or email ID is already exists"));
                                notStoredData.add(bulkUploadDto);
                            }

                        }
                    } else {
                        bulkUploadDto.setStatus(true);
                        bulkUploadDto.setRemark(List.of("Current role does not exist."));
                        notStoredData.add(bulkUploadDto);
                    }
                } else {
                    bulkUploadDto.setStatus(true);
                    notStoredData.add(bulkUploadDto);
                }
            } else {
                bulkUploadDto.setStatus(true);
                notStoredData.add(bulkUploadDto);
            }
        }

        // Return both the stored and not stored data
        List<EmployeeBulkUploadDto> allData = new ArrayList<>();
        allData.addAll(storedData);
        allData.addAll(notStoredData);
        
        logger.info("Currect employee data list --- "+storedData);
        logger.info("According to validation incorrect employee data list  --- "+notStoredData);
        logger.info("Excel data convert into json with status and remark --- "+allData);
        
        return allData;
    }


    
//    private List<EmployeeBulkUploadDto> processEmployeeSheet(List<EmployeeBulkUploadDto> employeeBulkUploadDto, User currentUser) throws Exception {
//        List<User> existingUsers = userRepository.findAll();
//        List<EmployeeBulkUploadDto> notStoredData = new ArrayList<>();
//        List<EmployeeBulkUploadDto> storedData = new ArrayList<>();
//
//        logger.info("Found List of users {}", employeeBulkUploadDto);
//
//        for (EmployeeBulkUploadDto bulkUploadDto : employeeBulkUploadDto) {
//            if (bulkUploadDto.getEmployeeId() != null &&
//                    bulkUploadDto.getFullName() != null &&
//                    bulkUploadDto.getDateOfJoining() != null &&
//                    bulkUploadDto.getDateOfBirth() != null &&
//                    bulkUploadDto.getCurrentRole() != null &&
//                    bulkUploadDto.getEmail() != null &&
//                    bulkUploadDto.getGender() != null &&
//                    bulkUploadDto.getMobile_number() != null &&
//                    bulkUploadDto.getLocation() != null) {
//
//                // Check if employee ID already exists
//                boolean existsByEmployeeId = userRepository.existsByEmployeeId(bulkUploadDto.getEmployeeId());
//
//                if (!existsByEmployeeId) {
//                    // Continue with the rest of the processing
//
//                    User existingUser = findUserByEmail(existingUsers, bulkUploadDto.getEmail());
//
//                    // Generate a random password
//                    String generatedPassword = generateRandomPassword();
//                    String encodedPassword = passwordEncoder.encode(generatedPassword);
//
//                    if (bulkUploadDto.getRemark().isEmpty()) {
//                        if (existingUser != null) {
//                            if (existingUser.is_deleted()) {
//                                // User exists but is marked as deleted, so create a new user
//                                createUser(existingUser, bulkUploadDto, generatedPassword, encodedPassword, currentUser);
//                                storedData.add(bulkUploadDto);
//                            } else {
//                                // User exists, update the existing user
//                                if (Objects.equals(existingUser.getEmployee_Id(), bulkUploadDto.getEmployeeId())) {
//                                    updateUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
//                                    storedData.add(bulkUploadDto);
//                                } else {
//                                    bulkUploadDto.setStatus(true);
//                                    bulkUploadDto.setRemark(List.of("Employee ID or email ID already exists"));
//                                    notStoredData.add(bulkUploadDto);
//                                }
//                            }
//                        } else {
//                            // User doesn't exist, create a new user
//                            boolean existsByEmail = userRepository.existsByEmail(bulkUploadDto.getEmail());
//                            if (!existsByEmail) {
//                                User newUser = new User();
//                                createUser(newUser, bulkUploadDto, generatedPassword, encodedPassword, currentUser);
//                                bulkUploadDto.setStatus(false);
//                                storedData.add(bulkUploadDto);
//                            } else {
//                                bulkUploadDto.setStatus(true);
//                                bulkUploadDto.setRemark(List.of("Employee ID or email ID already exists"));
//                                notStoredData.add(bulkUploadDto);
//                            }
//                        }
//                    } else {
//                        bulkUploadDto.setStatus(true);
//                        notStoredData.add(bulkUploadDto);
//                    }
//
//                } else {
//                    // Employee ID already exists, set status to true and add a remark
//                    bulkUploadDto.setStatus(true);
//                    bulkUploadDto.setRemark(List.of("Employee ID already exists"));
//                    notStoredData.add(bulkUploadDto);
//                }
//
//            } else {
//                // Missing required fields, set status to true and add to notStoredData
//                bulkUploadDto.setStatus(true);
//                notStoredData.add(bulkUploadDto);
//            }
//        }
//
//        // Return both the stored and not stored data
//        List<EmployeeBulkUploadDto> allData = new ArrayList<>();
//        allData.addAll(storedData);
//        allData.addAll(notStoredData);
//
//        return allData;
//    }


    private boolean allFieldsAreNull(EmployeeBulkUploadDto dto) {
        return dto.getEmployee_id() == null &&
                dto.getFullName() == null &&
                dto.getDateOfJoining() == null &&
                dto.getDateOfBirth() == null &&
                dto.getCurrentRole() == null &&
                dto.getEmail_id() == null &&
                dto.getGender() == null &&
                dto.getMobile_number() == null &&
                dto.getLocation() == null &&
                dto.getAppRoleId() == null;
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
    
    
    /**
     * Creates a new employee with the provided data.
     *
     * @param newEmployee     The new employee to create.
     * @param bulkUploadDto The data from the uploaded Excel sheet.
     * @param currentUser  The user initiating the creation.
     */

    private User createUser(User newUser, EmployeeBulkUploadDto bulkUploadDto, String generatedPassword, String encodedPassword, User currentUser, Principal principal) throws Exception {
        
    	 Roles currentRole = rolesRepository.findByRoleName(bulkUploadDto.getCurrentRole());
    	 
    	newUser.setFull_name(bulkUploadDto.getFullName());
        newUser.setDate_of_joining(bulkUploadDto.getDateOfJoining());
        newUser.setDate_of_birth(bulkUploadDto.getDateOfBirth());
        newUser.setCurrent_role(bulkUploadDto.getCurrentRole());
        newUser.setEmail(bulkUploadDto.getEmail_id());
        newUser.setEmployee_Id(bulkUploadDto.getEmployee_id());
        newUser.setGender(bulkUploadDto.getGender());
        newUser.setMobile_number(bulkUploadDto.getMobile_number());
        newUser.setLocation(bulkUploadDto.getLocation());
        newUser.setPassword(encodedPassword);
        newUser.setModified_by(currentUser.getUser_id());
        newUser.setModified_on(LocalDateTime.now());
        
        Long appRoleIdLong = Long.parseLong(bulkUploadDto.getAppRoleId());
        Integer appRoleId = appRoleIdLong.intValue();
        newUser.setAppRole(roleRepository.findById(appRoleId).orElse(null));
        //newUser.setAppRole(roleRepository.findById(Long.parseLong(bulkUploadDto.getAppRoleId())).orElse(null));
        
     // Set app role as ROLE_USER by default
//        newUser.setAppRole(roleRepository.findByName(ERole.ROLE_USER));
        
        User user = userRepository.save(newUser);
     // Map the user to the specified role
        mapUserToRole(user, currentRole);
	     
	     UserToJsonConverter userToJsonConverter = new UserToJsonConverter();
	     
	     String newData = userToJsonConverter.convertUserToJSON(newUser);
	     
	     ActivityHistory activityHistory = new ActivityHistory();
		 activityHistory.setActivity_type("Bulk upload");
		 activityHistory.setDescription("Bulk upload of employees");
		 activityHistory.setNew_data(newData);
		
		 activityHistoryService.addActivity(activityHistory, principal);
        // Send the email with the generated password
        sendEmailPassword(newUser, generatedPassword);
        
        return user;

    }
    
    private void mapUserToRole(User user, Roles role) {
        UserRolesMapping userRoleMapping = new UserRolesMapping();
        userRoleMapping.setUser(user);
        userRoleMapping.setRole(role);
        userRolesMappingRepository.save(userRoleMapping);
    }
    
    /**
     * Updates an existing employee with the provided data.
     *
     * @param existingEmployee   The existing employee to update.
     * @param bulkUploadDto The data from the uploaded Excel sheet.
     * @param currentUser    The user initiating the update.
     */

    private User updateUser(User existingUser, EmployeeBulkUploadDto bulkUploadDto, String encodedPassword, User currentUser) {
        existingUser.setFull_name(bulkUploadDto.getFullName());
        existingUser.setDate_of_joining(bulkUploadDto.getDateOfJoining());
        existingUser.setDate_of_birth(bulkUploadDto.getDateOfBirth());
        existingUser.setCurrent_role(bulkUploadDto.getCurrentRole());
        existingUser.setGender(bulkUploadDto.getGender());
        existingUser.setEmployee_Id(bulkUploadDto.getEmployee_id());
        existingUser.setMobile_number(bulkUploadDto.getMobile_number());
        existingUser.setLocation(bulkUploadDto.getLocation());
        existingUser.setPassword(encodedPassword);
        existingUser.setModified_by(currentUser.getUser_id());
        existingUser.setModified_on(LocalDateTime.now());
        
        Long appRoleIdLong = Long.parseLong(bulkUploadDto.getAppRoleId());
        Integer appRoleId = appRoleIdLong.intValue();
        existingUser.setAppRole(roleRepository.findById(appRoleId).orElse(null));
        //existingUser.setAppRole(roleRepository.findById(Long.parseLong(bulkUploadDto.getAppRoleId())).orElse(null));
        
        bulkUploadDto.setRemark(List.of("Update the employee data."));
        return userRepository.save(existingUser);
    }
    
    
    /**
     * Finds a employee by its email in the list of existing.
     *
     * @param employeeList  The list of existing employees.
     * @param email The email of the employee to find.
     * @return The found employee, or null if not found.
     */

    private User findUserByEmail(List<User> userList, String Email) {
        for (User employee : userList) {
            if (employee.getEmail() != null && employee.getEmail().equals(Email) && employee.is_deleted() == false ) {
                return employee;
            }
        }
        return null;
    }
    
    private User findUserByEmailAndEmployeeId(List<User> userList, String email, String employeeId) {
        for (User employee : userList) {
            if (employee.getEmail() != null && employee.getEmail().equals(email) &&
                employee.getEmployee_Id() != null && employee.getEmployee_Id().equals(employeeId) &&
                employee.is_deleted()==false) {
                return employee;
            }
        }
        return null;
    }

	    
    
    /**
     * Validates and processes data from an Excel sheet containing employee information.
     *
     * @param sheet The Excel sheet containing employees data.
     * @return A list of EmployeeBulkUploadDto objects with processed employee data.
     */
    
    private List<EmployeeBulkUploadDto> validateEmployeeData(Sheet sheet) {
        Set<String> processedEmployeeIds1 = new HashSet<>();
        Set<String> processedEmails = new HashSet<>();
        List<String> missingDataMsg = new ArrayList<>();

        List<EmployeeBulkUploadDto> bulkExcelEmployeeDtos = new ArrayList<>();

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
            String gender = getStringValue(row.getCell(7)); // gender
            String mobile_number = getStringValue(row.getCell(8)); // mobile_number
            String location = getStringValue(row.getCell(9)); // location
            String appRoleId = getStringValue(row.getCell(10));

            if (employeeId == null || employeeId.isEmpty()) {
                missingDataMsg.add("Data missing for employee Id");
            }
            if (fullName == null || fullName.isEmpty()) {
                missingDataMsg.add("Data missing for employee full name");
            }
            if (dateOfJoining == null || dateOfJoining.isEmpty()) {
                missingDataMsg.add("Data missing for date of joining");
            }
            if (dateOfBirth == null || dateOfBirth.isEmpty()) {  //optional
                missingDataMsg.add("Data missing for date of birth");
            }
            if (currentRole == null || currentRole.isEmpty()) {
                missingDataMsg.add("Data missing for current role");
            }
            if (email == null || email.isEmpty()) {
                missingDataMsg.add("Data missing for email");
            }
            if (appRoleId == null || appRoleId.isEmpty()) {
                missingDataMsg.add("Data missing for app role id");
            }

            if (processedEmployeeIds1.contains(employeeId)) {
                missingDataMsg.add("Duplicate data entry for employee Id: " + employeeId);
            } else {
                processedEmployeeIds1.add(employeeId);
            }

            if (processedEmails.contains(email)) {
                missingDataMsg.add("Duplicate data entry for email: " + email);
            } else {
                processedEmails.add(email);
            }
            
            if (!isValidAppRoleId(appRoleId)) {
                missingDataMsg.add("Application role does not valid");
            }
            
//            if (userRepository.existsByEmail(email) || userRepository.existsByEmployeeId(employeeId)) {
//                missingDataMsg.add("Employee ID or email ID already exists in the database");
//            }

            missingDataMsg.stream().distinct().toList();
            logger.info("Msg list for error message: " + missingDataMsg);
            EmployeeBulkUploadDto bulkExcelEmployeeDto = new EmployeeBulkUploadDto();
            bulkExcelEmployeeDto.setEmployee_id(employeeId);
            bulkExcelEmployeeDto.setFullName(fullName);
            bulkExcelEmployeeDto.setDateOfJoining(dateOfJoining);
            bulkExcelEmployeeDto.setDateOfBirth(dateOfBirth);
            bulkExcelEmployeeDto.setCurrentRole(currentRole);
            bulkExcelEmployeeDto.setEmail_id(email);
            bulkExcelEmployeeDto.setGender(gender);
            bulkExcelEmployeeDto.setMobile_number(mobile_number);
            bulkExcelEmployeeDto.setLocation(location);
            bulkExcelEmployeeDto.setRemark(missingDataMsg.stream().toList());
            bulkExcelEmployeeDto.setStatus(false);
            bulkExcelEmployeeDto.setAppRoleId(appRoleId);
            
           if (!allFieldsAreNull(bulkExcelEmployeeDto)){
                bulkExcelEmployeeDtos.add(bulkExcelEmployeeDto);
            }

            missingDataMsg.clear();

        }
        logger.error("Bulk Excel Employee Dto received {}", bulkExcelEmployeeDtos);
        System.out.println("Bulk Excel Employee Dtos = " + bulkExcelEmployeeDtos.size());
        return bulkExcelEmployeeDtos;
    }

    private boolean isValidAppRoleId(String appRoleId) {
        try {
            // Convert the appRoleId from String to long
            long appRoleIdLong = Long.parseLong(appRoleId);
            
            // Perform your validation logic here
            // For example, check if the appRoleId is 1 or 2
            return appRoleIdLong == 1 || appRoleIdLong == 2;
        } catch (NumberFormatException e) {
            // Handle the case where appRoleId cannot be parsed to long
            return false;
        }
    }
    
    /**
     * Generates a random password with specified complexity.
     *
     * @return A randomly generated password.
     */

    public String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChars = "abcdefghijklmnopqrstuvwxyz";
        String specialChars = "!@#$%^&*()";
        String digits = "0123456789";

        StringBuilder password = new StringBuilder();
        password.append(upperChars.charAt(random.nextInt(upperChars.length())));
        password.append(lowerChars.charAt(random.nextInt(lowerChars.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));

        // at least 8 characters password
        for (int i = 4; i < 8; i++) {
            String allChars = upperChars + lowerChars + specialChars + digits;
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        return password.toString();
    }

    
    /**
     * Sends an email to a user with their generated password.
     *
     * @param user              The user to send the email to.
     * @param generatePassword The generated password.
     * @throws Exception If there is an error sending the email.
     */

    public void sendEmailPassword(User user, String generatePassword) throws Exception {
        String senderName = "QW Resume Builder";
        String subject = "Credential details";
        String content = "Dear " + user.getFull_name() + ",<br>"
                + "We have generated a login credential. Please use the following username and password to login QW Resume Builder:<br><br>"
                + "Username: " + user.getEmail() + "<br>"
                + "New Password: " + generatePassword + "<br>";
        content += "<p>Thank you,<br>" + "QW Resume Builder.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("pratikshawakekar94@gmail.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

}