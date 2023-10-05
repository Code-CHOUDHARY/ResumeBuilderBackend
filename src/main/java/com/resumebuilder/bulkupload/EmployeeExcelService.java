package com.resumebuilder.bulkupload;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resumebuilder.DTO.UserDto;
import com.resumebuilder.reportingmanager.ReportingManager;
import com.resumebuilder.reportingmanager.ReportingManagerRepository;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import io.micrometer.common.util.StringUtils;

@Service
public class EmployeeExcelService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReportingManagerRepository managerRepository;
	
	//public void save(MultipartFile file) throws IOException {
	   /* List<UserDto> users = ExcelHelper.convertExcelToListofEmployee(file.getInputStream());
	    for (UserDto user : users) {
	        User userSave = new User();

	        // Check for null values in required fields
	        if (StringUtils.isBlank(user.getEmployee_Id()) ||
	                StringUtils.isBlank(user.getFull_name()) ||
	                StringUtils.isBlank(user.getDate_of_joining()) ||
	                StringUtils.isBlank(user.getDate_of_birth()) ||
	                StringUtils.isBlank(user.getCurrent_role()) ||
	                StringUtils.isBlank(user.getEmail()) ||
	                StringUtils.isBlank(user.getGender()) ||
	                StringUtils.isBlank(user.getMobile_number()) ||
	                StringUtils.isBlank(user.getLocation())) {
	                userSave.setRemark("Data missing");
	                userSave.setStatus(false);
	        } else {
	            // Check for duplicate email
//	            User existingUser = userRepository.findByEmail(user.getEmail());
//	            if (existingUser != null) {
//	                userSave.setRemark("Duplicate email data entry");
//	                userSave.setStatus(false);
//	            } else {
//	                // Check for duplicate employee_Id
//	                User existingEmployee = userRepository.findByEmployeeId(user.getEmployee_Id());
//	                if (existingEmployee != null) {
//	                    userSave.setRemark("Duplicate Employee Id data entry");
//	                    userSave.setStatus(false);
//	                } else {
//	                    // Data is correct, set status to true
//	                    userSave.setStatus(true);
//	                }
//	            }
	        	List<User> existingUser = userRepository.findByEmailIds(user.getEmail());
	        	List<User> existingEmployee = userRepository.findByEmployeeId(user.getEmployee_Id());

	        	if (!existingUser.isEmpty() || !existingEmployee.isEmpty()) {
	        	    userSave.setRemark("Duplicate data entry");
	        	    userSave.setStatus(false);
	        	} else {
	        	    // Data is correct, set status to true
	        	    userSave.setStatus(true);
	        	}
	        }

	        // Set other fields
	        userSave.setEmployee_Id(user.getEmployee_Id());
	        userSave.setFull_name(user.getFull_name());
	        userSave.setDate_of_joining(user.getDate_of_joining());
	        userSave.setDate_of_birth(user.getDate_of_birth());
	        userSave.setCurrent_role(user.getCurrent_role());
	        userSave.setEmail(user.getEmail());
	        userSave.setGender(user.getGender());
	        userSave.setMobile_number(user.getMobile_number());
	        userSave.setLocation(user.getLocation());

	        // Save the User entity
	        User savedUser = userRepository.save(userSave);

	        // Create and save ReportingManager entity
	        ReportingManager manager = new ReportingManager();
	        manager.setEmployee_id(savedUser.getEmployee_Id());
	        manager.setManager_employee_id(user.getManager_employee_id());
	        managerRepository.save(manager);
	    } */
		
	public void save(MultipartFile file) throws IOException {
		List<UserDto> users = EmployeeExcelHelper.convertExcelToListofEmployee(file.getInputStream());
		for (UserDto user : users) {
		    // Check if a record with the same Employee ID or Email already exists
		    User existingUser = userRepository.findByEmployeeIds(user.getEmployee_Id());
		    if (existingUser == null) {
		        existingUser = userRepository.findByEmail(user.getEmail());
		    }

		    User userSave;
		    
		    if (existingUser != null) {
		        // If the record exists, update it
		        userSave = existingUser;
		    } else {
		        // If the record does not exist, create a new one
		        userSave = new User();
		    }
		    
		 // Set other fields
		    userSave.setEmployee_Id(user.getEmployee_Id());
		    userSave.setFull_name(user.getFull_name());
		    userSave.setDate_of_joining(user.getDate_of_joining());
		    userSave.setDate_of_birth(user.getDate_of_birth());
		    userSave.setCurrent_role(user.getCurrent_role());
		    userSave.setEmail(user.getEmail());
		    userSave.setGender(user.getGender());
		    userSave.setMobile_number(user.getMobile_number());
		    userSave.setLocation(user.getLocation());

		    // Save the User entity
		    User savedUser = userRepository.save(userSave);

//		    // Check for null values in required fields
//		    if (StringUtils.isBlank(user.getEmployee_Id()) ||
//		            StringUtils.isBlank(user.getFull_name()) ||
//		            StringUtils.isBlank(user.getDate_of_joining()) ||
//		            StringUtils.isBlank(user.getDate_of_birth()) ||
//		            StringUtils.isBlank(user.getCurrent_role()) ||
//		            StringUtils.isBlank(user.getEmail()) ||
//		            StringUtils.isBlank(user.getGender()) ||
//		            StringUtils.isBlank(user.getMobile_number()) ||
//		            StringUtils.isBlank(user.getLocation())) {
//		        userSave.setRemark("Data missing");
//		        userSave.setStatus(false);
//		    } else {
//		        // Data is correct, set status to true
//		        userSave.setStatus(true);
//		    }

		    
		 // Check if a ReportingManager entry already exists based on employee_id
		    ReportingManager existingManager = managerRepository.findByEmployee_id(savedUser.getEmployee_Id());
		    ReportingManager manager = new ReportingManager();
		    manager.setEmployee_id(savedUser.getEmployee_Id());
		    manager.setManager_employee_id(user.getManager_employee_id());

		    if (existingManager != null) {
		        // If the manager entry already exists, update it
		        manager.setId(existingManager.getId()); // Set the ID to update the existing entry
		    }

		    managerRepository.save(manager);
		}

		
	}

	
	public List<User> getAllUsers(){
		return this.userRepository.findAll();
		
	}

}