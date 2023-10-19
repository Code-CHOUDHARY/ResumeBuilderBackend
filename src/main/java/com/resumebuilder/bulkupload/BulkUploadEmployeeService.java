package com.resumebuilder.bulkupload;

import com.resumebuilder.exception.DataProcessingException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;
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
import java.lang.reflect.Field;
import java.security.Principal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BulkUploadEmployeeService {

    public static final Logger logger = LogManager.getLogger(BulkUploadEmployeeService.class);

    public static final String EXCEL_TEMPLATE_DIRECTORY = "upload/template/employee";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Map<Integer, String> employeeColumnMapping; // Inject the mapping for Employee


	    /*@Transactional
	    public void processEmployeeExcelFile(MultipartFile file, Principal principal) throws Exception {

	    	try {
				// Save the uploaded Excel file to the project path
	            String fileName = file.getOriginalFilename();
	            File destFile = new File(EXCEL_TEMPLATE_DIRECTORY, fileName);

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

	            logger.info("Number of Rows: " + employeeSheet.getPhysicalNumberOfRows());

	            // Process and save data from employeeSheet
	            List<String> checkValid = validateEmployeeData(employeeSheet);

	            User user = userRepository.findByEmailId(principal.getName());

	            if (!checkValid.isEmpty()) {
	            	String errorMessage = String.join(", ", checkValid);
	            	logger.error("Validation Error: " + errorMessage);
		            throw new DataProcessingException(errorMessage);
		        }
					processEmployeeSheet(employeeSheet, employeeColumnMapping, user);

	        } catch (java.io.IOException e) {
	            e.printStackTrace();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    }*/

    // =============second=========


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
            return processEmployeeSheet(employeeBulkUploadDtos, user);
        } catch (IOException e) {
            e.printStackTrace(); // You should consider proper error handling (e.g., logging or throwing a custom exception)
            throw e; // Rethrow the exception for error reporting or handling at a higher level.
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


    /*private void processEmployeeSheet(Sheet sheet, Map<Integer, String> columnMapping, User currentUser) throws Exception {
        List<User> existingUsers = userRepository.findAll();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Skip the header row
                continue;
            }

            String employeeId = getStringValue(row.getCell(1));
            String fullName = getStringValue(row.getCell(2));
            String dateOfJoining = getStringValue(row.getCell(3));
            String dateOfBirth = getStringValue(row.getCell(4));
            String currentRole = getStringValue(row.getCell(5));
            String email = getStringValue(row.getCell(6));

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

            User existingUser = findUserByEmail(existingUsers, user.getEmail());
            // Generate a random password
            String generatedPassword = generateRandomPassword();
            String encodedPassword = passwordEncoder.encode(generatedPassword);

            if (existingUser == null) {
                user.setPassword(encodedPassword);
                user.setModified_by(currentUser.getFull_name());
                user.setModified_on(user.getModified_on());
                userRepository.save(user);
                sendEmailPassword(user, generatedPassword);
            } else if (existingUser.is_deleted()) {
                User newUser = new User();
                newUser.setFull_name(user.getFull_name());
                newUser.setDate_of_joining(user.getDate_of_joining());
                newUser.setDate_of_birth(user.getDate_of_birth());
                newUser.setCurrent_role(user.getCurrent_role());
                newUser.setEmail(user.getEmail());
                newUser.setGender(user.getGender());
                newUser.setMobile_number(user.getMobile_number());
                newUser.setLocation(user.getLocation());
                newUser.setPassword(encodedPassword);
                newUser.setModified_by(currentUser.getFull_name());
                newUser.setModified_on(user.getModified_on());


                userRepository.save(newUser);
                sendEmailPassword(user, generatedPassword);
            }
        }
    }*/

    /*private List<EmployeeBulkUploadDto> processEmployeeSheet(List<EmployeeBulkUploadDto> employeeBulkUploadDto, User currentUser) throws Exception {
        List<User> existingUsers = userRepository.findAll();

        logger.info("Found List of users {}", employeeBulkUploadDto);
        for (EmployeeBulkUploadDto bulkUploadDto : employeeBulkUploadDto) {
            User existingUser = findUserByEmail(existingUsers, bulkUploadDto.getEmail());
            // Generate a random password
            String generatedPassword = generateRandomPassword();
            String encodedPassword = passwordEncoder.encode(generatedPassword);

            if (existingUser != null) {
                if (existingUser.is_deleted()) {
                    // User exists but is marked as deleted, so create a new user
                    createUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
                } else {
                    // User exists, update the existing user
                    updateUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
                }
            } else {
                // User doesn't exist, create a new user
                User newUser = new User();
                createUser(newUser, bulkUploadDto, encodedPassword, currentUser);
            }
        }
        logger.info("User saved successfully {}", employeeBulkUploadDto);

        return employeeBulkUploadDto.stream().sorted(Comparator.comparing(EmployeeBulkUploadDto::isStatus)).toList();
    }*/

    private List<EmployeeBulkUploadDto> processEmployeeSheet(List<EmployeeBulkUploadDto> employeeBulkUploadDto, User currentUser) throws Exception {
        List<User> existingUsers = userRepository.findAll();
        List<EmployeeBulkUploadDto> notStoredData = new ArrayList<>(); // To store data that shouldn't be stored

        logger.info("Found List of users {}", employeeBulkUploadDto);
        for (EmployeeBulkUploadDto bulkUploadDto : employeeBulkUploadDto) {
            if (bulkUploadDto.getEmployeeId() != null &&
                    bulkUploadDto.getFullName() != null &&
                    bulkUploadDto.getDateOfJoining() != null &&
                    bulkUploadDto.getDateOfBirth() != null &&
                    bulkUploadDto.getCurrentRole() != null &&
                    bulkUploadDto.getEmail() != null &&
                    bulkUploadDto.getGender() != null &&
                    bulkUploadDto.getMobile_number() != null &&
                    bulkUploadDto.getLocation() != null &&
                    bulkUploadDto.getRemark() != null) {
                User existingUser = findUserByEmail(existingUsers, bulkUploadDto.getEmail());
                // Generate a random password
                String generatedPassword = generateRandomPassword();
                String encodedPassword = passwordEncoder.encode(generatedPassword);

                if (existingUser != null) {
                    if (existingUser.is_deleted()) {
                        // User exists but is marked as deleted, so create a new user
                        createUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
                    } else {
                        // User exists, update the existing user
                        updateUser(existingUser, bulkUploadDto, encodedPassword, currentUser);
                    }
                } else {
                    // User doesn't exist, create a new user
                    User newUser = new User();
                    createUser(newUser, bulkUploadDto, encodedPassword, currentUser);
                }

                // Add this record to the list of stored data
                notStoredData.add(bulkUploadDto);
            } else {
                // Handle data that is null and shouldn't be stored
                notStoredData.add(bulkUploadDto);
            }
        }
        return notStoredData;
    }



    private void createUser(User newUser, EmployeeBulkUploadDto bulkUploadDto, String encodedPassword, User currentUser) {
        newUser.setFull_name(bulkUploadDto.getFullName());
        newUser.setDate_of_joining(bulkUploadDto.getDateOfJoining());
        newUser.setDate_of_birth(bulkUploadDto.getDateOfBirth());
        newUser.setCurrent_role(bulkUploadDto.getCurrentRole());
        newUser.setEmail(bulkUploadDto.getEmail());
        newUser.setGender(bulkUploadDto.getGender());
        newUser.setMobile_number(bulkUploadDto.getMobile_number());
        newUser.setLocation(bulkUploadDto.getLocation());
        newUser.setPassword(encodedPassword);
        newUser.setModified_by(currentUser.getFull_name());
        newUser.setModified_on(LocalDateTime.now());

        userRepository.save(newUser);
       /* try {
            sendEmailPassword(newUser, encodedPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    private void updateUser(User existingUser, EmployeeBulkUploadDto bulkUploadDto, String encodedPassword, User currentUser) {
        existingUser.setFull_name(bulkUploadDto.getFullName());
        existingUser.setDate_of_joining(bulkUploadDto.getDateOfJoining());
        existingUser.setDate_of_birth(bulkUploadDto.getDateOfBirth());
        existingUser.setCurrent_role(bulkUploadDto.getCurrentRole());
        existingUser.setGender(bulkUploadDto.getGender());
        existingUser.setMobile_number(bulkUploadDto.getMobile_number());
        existingUser.setLocation(bulkUploadDto.getLocation());
        existingUser.setPassword(encodedPassword);
        existingUser.setModified_by(currentUser.getFull_name());
        existingUser.setModified_on(LocalDateTime.now());

        userRepository.save(existingUser);
        /*try {
            sendEmailPassword(existingUser, encodedPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    private User findUserByEmail(List<User> userList, String Email) {
        for (User employee : userList) {
            if (employee.getEmail() != null && employee.getEmail().equals(Email) && employee.is_deleted() == false) {
                return employee;
            }
        }
        return null;
    }
	    
	    /*private List<String> validateEmployeeData(Sheet sheet) {
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

	            if (processedEmployeeIds1.contains(employeeId)) {
	                duplicateDataMsg.add("Duplicate data entry for employee Id: " + employeeId);
	            } else {
	                processedEmployeeIds1.add(employeeId);
	            }

	            if (processedEmails.contains(email)) {
	                duplicateDataMsg.add("Duplicate data entry for email: " + email);
	            } else {
	                processedEmails.add(email);
	            }

	            validationMessages.addAll(missingDataMsg);
	            validationMessages.addAll(duplicateDataMsg);
	        }

	        return validationMessages;
	    }*/

    private List<EmployeeBulkUploadDto> validateEmployeeData(Sheet sheet) {
        Set<String> processedEmployeeIds1 = new HashSet<>();
        Set<String> processedEmails = new HashSet<>();
        List<String> missingDataMsg = new ArrayList<>();

        List<EmployeeBulkUploadDto> bulkExcelEmployeeDtos = new ArrayList<>();

        for (Row row : sheet) {
            String employeeId = getStringValue(row.getCell(1)); // Assuming EMP ID is in column 1
            String fullName = getStringValue(row.getCell(2)); // Employee Full Name
            String dateOfJoining = getStringValue(row.getCell(3)); // DoJ
            String dateOfBirth = getStringValue(row.getCell(4)); // DoB
            String currentRole = getStringValue(row.getCell(5)); // Current Role
            String email = getStringValue(row.getCell(6)); // Email
            String gender = getStringValue(row.getCell(7)); // gender
            String mobile_number = getStringValue(row.getCell(8)); // mobile_number
            String location = getStringValue(row.getCell(9)); // location

            boolean isValidRecord = true;

            if (employeeId == null || employeeId.isEmpty()) {
                missingDataMsg.add("Data missing for employee Id");
                isValidRecord = false;
            }
            if (fullName == null || fullName.isEmpty()) {
                missingDataMsg.add("Data missing for employee full name");
                isValidRecord = false;
            }
            if (dateOfJoining == null || dateOfJoining.isEmpty()) {
                missingDataMsg.add("Data missing for date of joining");
                isValidRecord = false;
            }
            if (dateOfBirth == null || dateOfBirth.isEmpty()) {  //optional
                missingDataMsg.add("Data missing for date of birth");
                isValidRecord = false;
            }
            if (currentRole == null || currentRole.isEmpty()) {
                missingDataMsg.add("Data missing for current role");
                isValidRecord = false;
            }
            if (email == null || email.isEmpty()) {
                missingDataMsg.add("Data missing for email");
                isValidRecord = false;
            }

            if (processedEmployeeIds1.contains(employeeId)) {
                missingDataMsg.add("Duplicate data entry for employee Id: " + employeeId);
                isValidRecord = false;
            } else {
                processedEmployeeIds1.add(employeeId);
            }

            if (processedEmails.contains(email)) {
                missingDataMsg.add("Duplicate data entry for email: " + email);
                isValidRecord = false;
            } else {
                processedEmails.add(email);
            }

            if (isValidRecord) {
                List<String> msgList = missingDataMsg.stream().distinct().toList();
                EmployeeBulkUploadDto bulkExcelEmployeeDto = new EmployeeBulkUploadDto();
                bulkExcelEmployeeDto.setEmployeeId(employeeId);
                bulkExcelEmployeeDto.setFullName(fullName);
                bulkExcelEmployeeDto.setDateOfJoining(dateOfJoining);
                bulkExcelEmployeeDto.setDateOfBirth(dateOfBirth);
                bulkExcelEmployeeDto.setCurrentRole(currentRole);
                bulkExcelEmployeeDto.setEmail(email);
                bulkExcelEmployeeDto.setGender(gender);
                bulkExcelEmployeeDto.setMobile_number(mobile_number);
                bulkExcelEmployeeDto.setLocation(location);
                bulkExcelEmployeeDto.setRemark(msgList);
                bulkExcelEmployeeDto.setStatus(false);

                  /*if (!missingDataMsg.isEmpty()) {
                bulkExcelEmployeeDto = new EmployeeBulkUploadDto(employeeId, fullName, dateOfJoining, dateOfBirth, currentRole, email, gender, mobile_number, location, null, false);
            } else {
                bulkExcelEmployeeDto = new EmployeeBulkUploadDto(employeeId, fullName, dateOfJoining, dateOfBirth, currentRole, email, gender, mobile_number, location, msgList, true);
            }*/

                bulkExcelEmployeeDtos.add(bulkExcelEmployeeDto);
            }
            missingDataMsg.clear();
        }

        System.out.println("bulkExcelEmployeeDtos = " + bulkExcelEmployeeDtos.size());
        return bulkExcelEmployeeDtos;
    }



    private void setFieldValue(Object object, String fieldName, String cellValue) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (field.getType().equals(String.class)) {
                field.set(object, cellValue);
            } else if (field.getType().equals(Integer.class)) {
                field.set(object, Integer.parseInt(cellValue));
            } else if (field.getType() == LocalDateTime.class) {
                LocalDateTime localDateTime = LocalDateTime.parse(cellValue);
                field.set(object, localDateTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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