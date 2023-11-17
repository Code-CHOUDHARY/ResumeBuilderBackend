package com.resumebuilder.user;

import java.io.File;
import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.resumebuilder.DTO.UserDto;
import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryRepository;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.auth.SignupRequest;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.reportingmanager.ReportingManager;
import com.resumebuilder.reportingmanager.ReportingManagerRepository;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;
import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.approle.UserRole;
import com.resumebuilder.security.response.MessageResponse;

import io.jsonwebtoken.lang.Objects;


@Service
public class UserServiceImplementation implements UserService{
	
	private final static Logger logger = LogManager.getLogger(UserServiceImplementation.class);
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RolesRepository rolesRepository;
	@Autowired
	private UserRolesMappingRepository usereRolesMappingRepository;
	
	 @Autowired
	  private UserRoleRepository roleRepository;
	 
	 @Autowired
	 private JavaMailSender mailSender;
	 
	 @Autowired
	 private ReportingManagerRepository reportingManagerRepository;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	 
	 	@Autowired
		private ActivityHistoryService activityHistoryService;
		
		@Autowired
		private ActivityHistoryRepository activityHistoryRepository;

	 /**
	     * Retrieve a list of all users.
	     *
	     * @return List of User entities.
	     */
	 
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	/**
     * Find a user by their user ID.
     *
     * @param userId User ID.
     * @return The User entity if found, or throw UserNotFoundException.
     */

	public User findUserByIdUser(Long userId) {
		Optional<User> opt =userRepository.findById(userId);		
			return opt.get();	
	}
	
	/**
     * Find a user by their username (email).
     *
     * @param userName Username (email).
     * @return The User entity if found, or throw UserNotFoundException.
     */

	@Override
	public User findUserByUsername(String userName) {
		
		Optional<User> opt = userRepository.findByEmail(userName);
		return opt.get();
	}

	/**
     * Add a new user or update an existing one.
     *
     * @param signUpRequest SignupRequest containing user details.
     * @param principal      Principal representing the authenticated user.
     * @return ResponseEntity with a success message or error.
     * @throws UserNotFoundException if adding or updating the user fails.
     */
	
	//add the new user
	
//	public ResponseEntity<?> addUser(SignupRequest signUpRequest, Principal principal) throws UserNotFoundException {
//
//		try {
//	        User currentuser = userRepository.findByEmailId(principal.getName());	        
//	        if (currentuser == null) {
//	            throw new UserNotFoundException("Current user not found.");
//	        }
//	        // Check if a user with the same email exists (soft-deleted or not)
//	        User existingUser = userRepository.findByEmailId(signUpRequest.getEmail());
//
//	        if (existingUser != null) {
//	            // If an existing user with the same email exists
//	            if (existingUser.is_deleted()) {
//	            	// Check if the employee_id is unique
//	            	List<User> usersWithDuplicateEmployeeId = userRepository.findByEmployeeId(signUpRequest.getEmployee_Id());
//	            	if (!usersWithDuplicateEmployeeId.isEmpty()) {
//	            	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Employee ID already exists."));
//	            	}
//	            		
//	            	// If it's soft-deleted, create a new user without overwriting the existing soft-deleted user
//	                User newUser = new User(signUpRequest.getEmail(), generateRandomPassword());
//	                newUser.setFull_name(signUpRequest.getFull_name());
//	                newUser.setEmployee_Id(signUpRequest.getEmployee_Id());
//	                newUser.setCurrent_role(signUpRequest.getCurrent_role());
//	                newUser.setUser_image(signUpRequest.getUser_image());
//	                newUser.setGender(signUpRequest.getGender());
//	                newUser.setMobile_number(signUpRequest.getMobile_number());
//	                newUser.setLocation(signUpRequest.getLocation());
//	                newUser.setDate_of_joining(signUpRequest.getDate_of_joining());
//	                newUser.setDate_of_birth(signUpRequest.getDate_of_birth());
//	                newUser.setLinkedin_lnk(signUpRequest.getLinkedin_lnk());
//	                newUser.setPortfolio_link(signUpRequest.getPortfolio_link());
//	                newUser.setBlogs_link(signUpRequest.getBlogs_link());
//	                newUser.setModified_by(currentuser.getUser_id());
//	                String strRoles = signUpRequest.getRole();
//	                UserRole appRole;
//
//	                if (strRoles == null) {
//	                    appRole = roleRepository.findByName(ERole.ROLE_USER);
//	                } else {
//	                    switch (strRoles) {
//	                        case "admin":
//	                            appRole = roleRepository.findByName(ERole.ROLE_ADMIN);
//	                            break;
//	                        case "manager":
//	                            appRole = roleRepository.findByName(ERole.ROLE_MANAGER);
//	                            break;
//	                        default:
//	                            appRole = roleRepository.findByName(ERole.ROLE_USER);
//	                    }
//	                }
//	                
//
//	                newUser.setAppRole(appRole);
//	                String encodedPassword = passwordEncoder.encode(newUser.getPassword());
//	                newUser.setPassword(encodedPassword);
//	                newUser.set_deleted(false); // Mark the user as not soft-deleted
//	                User user = userRepository.save(newUser);
//	                
//	             // Create a directory for the new user
//	                createUserDirectory(signUpRequest.getEmployee_Id());
//	                
//	                for(Long id: signUpRequest.getManagerIds()) {	           	                	
//	                	User manager = userRepository.findById(id).get();	                	
//	                	System.out.println("manager id - "+manager);
//	                	 ReportingManager reportingManager = new ReportingManager();	      
//	                	 reportingManager.setEmployee(user);
//	                	 reportingManager.setManager(manager);
//	                	 reportingManagerRepository.save(reportingManager);	                		                	 
//	                }
//	                           	                
//	                // Send the email with the generated password
//	                sendEmailPassword(newUser, newUser.getPassword());
//
//	                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee data added successfully."));
//	            } else {
//	                // User with the same email already exists and is not soft-deleted
//	                throw new UserNotFoundException("User with this email already exists.");
//	            }
//	        } else {
//	        	
//	        	// Check if the employee_id is unique
//	        	List<User> usersWithDuplicateEmployeeId = userRepository.findByEmployeeId(signUpRequest.getEmployee_Id());
//	        	if (!usersWithDuplicateEmployeeId.isEmpty()) {
//	        	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Employee ID already exists."));
//	        	}
//	            // Create a new user
//	            String password = generateRandomPassword();
//	            User newUser = new User(signUpRequest.getEmail(), password);
//	            newUser.setFull_name(signUpRequest.getFull_name());
//	            newUser.setEmployee_Id(signUpRequest.getEmployee_Id());
//	            newUser.setCurrent_role(signUpRequest.getCurrent_role());
//	            newUser.setUser_image(signUpRequest.getUser_image());
//	            newUser.setGender(signUpRequest.getGender());
//	            newUser.setMobile_number(signUpRequest.getMobile_number());
//	            newUser.setLocation(signUpRequest.getLocation());
//	            newUser.setDate_of_joining(signUpRequest.getDate_of_joining());
//	            newUser.setDate_of_birth(signUpRequest.getDate_of_birth());
//	            newUser.setLinkedin_lnk(signUpRequest.getLinkedin_lnk());
//	            newUser.setPortfolio_link(signUpRequest.getPortfolio_link());
//	            newUser.setBlogs_link(signUpRequest.getBlogs_link());
//	            newUser.setModified_by(currentuser.getUser_id());
//
//	            String strRoles = signUpRequest.getRole();
//	            UserRole appRole;
//
//	            if (strRoles == null) {
//	                appRole = roleRepository.findByName(ERole.ROLE_USER);
//	            } else {
//	                switch (strRoles) {
//	                    case "admin":
//	                        appRole = roleRepository.findByName(ERole.ROLE_ADMIN);
//	                        break;
//	                    case "manager":
//	                        appRole = roleRepository.findByName(ERole.ROLE_MANAGER);
//	                        break;
//	                    default:
//	                        appRole = roleRepository.findByName(ERole.ROLE_USER);
//	                }
//	            }
//	            	            
//
//	            newUser.setAppRole(appRole);
//	            String encodedPassword = passwordEncoder.encode(newUser.getPassword());
//	            newUser.setPassword(encodedPassword);
//
//	            User user = userRepository.save(newUser);
//	         // Create a directory for the new user
//                createUserDirectory(signUpRequest.getEmployee_Id());
//                for(Long id: signUpRequest.getManagerIds()) {             	
//                	User manager = userRepository.findById(id).get();
//                	 ReportingManager reportingManager = new ReportingManager();      
//                	 reportingManager.setEmployee(user);
//                	 reportingManager.setManager(manager);
//                	 reportingManagerRepository.save(reportingManager);              	 
//                }
//
//	            // Send the email with the generated password
//	            sendEmailPassword(newUser, password);
//	            
//	            // Activity history logic
//	            
//	            UserToJsonConverter userToJsonConverter = new UserToJsonConverter();
//				
//				 String activityType = "Add Employee";
//			     String description = "New Employee Added";
//			     String newData = userToJsonConverter.convertUserToJSON(newUser);
//			     activityHistoryService.addActivity(activityType, description, newData, null, currentuser.getFull_name());
//
//	            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee data added successfully."));
//	        }
//	    } catch (Exception e) {
//	        throw new UserNotFoundException("Failed to add/update user data. " + e.getMessage());
//	    }
//	}
//
//	private void createUserDirectory(String employeeId) {
//		try {
//	        String baseDirectory = File.separator + "upload/";
//	        String sanitizedEmployeeId = employeeId.replaceAll("[^a-zA-Z0-9_]", "_");
//	        String userFolder = baseDirectory + File.separator + sanitizedEmployeeId;
//
//	        File userDirectory = new File(userFolder);
//	        if (!userDirectory.exists()) {
//	            if (userDirectory.mkdirs()) {
//	                logger.info("User directory created successfully: " + userDirectory.getAbsolutePath());
//	            } else {
//	                logger.error("Failed to create user directory: " + userDirectory.getAbsolutePath());
//	                // Handle the failure to create the directory or log the error message.
//	            }
//	        } else {
//	            logger.info("User directory already exists: " + userDirectory.getAbsolutePath());
//	        }
//	    } catch (Exception e) {
//	        logger.error("Error creating user directory: " + e.getMessage());
//	        // Handle the error or log the error message here.
//	    }
//	}
	
	@Override
	public ResponseEntity<?> addUser(SignupRequest signUpRequest, Principal principal) throws UserNotFoundException {
		logger.info("Adding a new user.");
		try {
			User currentuser = userRepository.findByEmailId(principal.getName());
			if (currentuser == null) {
				throw new UserNotFoundException("Current user not found.");
			}
			
			// Check if the provided current_role exists in the roles table
	        String currentRoleName = signUpRequest.getCurrent_role();
	        Roles currentRole = rolesRepository.findByRoleName(currentRoleName);
	        if (currentRole == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(new MessageResponse("Current role does not exist."));
	        }
			
			// Check if a user with the same email exists (soft-deleted or not)
			User existingUser = userRepository.findByEmailId(signUpRequest.getEmail());

			if (existingUser != null) {
				if (existingUser.is_deleted()) {
//					List<Roles> allRoles = rolesRepository.findAll();
//                    Roles currentRole = allRoles.stream()
//                            .filter(role -> role.getRole_name().equals(signUpRequest.getCurrent_role()))
//                            .findFirst()
//                            .orElse(null);
//                    if (currentRole != null) {
					
					
					boolean usersWithDuplicateEmployeeId = userRepository
							.existsByEmployeeId(signUpRequest.getEmployee_Id());
					boolean userWithDuplicateEmailId = userRepository.existsByEmail(signUpRequest.getEmail());
					boolean existBySoftDelete = userRepository
							.existsByEmployeeIdAndNotDeleted(signUpRequest.getEmployee_Id());

					if (!usersWithDuplicateEmployeeId && !userWithDuplicateEmailId) {
						if (!existBySoftDelete) {
							// If it's soft-deleted, create a new user without overwriting the existing						
							User newUser = new User(signUpRequest.getEmail(), generateRandomPassword());
							newUser.setFull_name(signUpRequest.getFull_name());
							newUser.setEmployee_Id(signUpRequest.getEmployee_Id());
							newUser.setCurrent_role(signUpRequest.getCurrent_role());
							newUser.setUser_image(signUpRequest.getUser_image());
							newUser.setGender(signUpRequest.getGender());
							newUser.setMobile_number(signUpRequest.getMobile_number());
							newUser.setLocation(signUpRequest.getLocation());
							newUser.setDate_of_joining(signUpRequest.getDate_of_joining());
							newUser.setDate_of_birth(signUpRequest.getDate_of_birth());
							newUser.setLinkedin_lnk(signUpRequest.getLinkedin_lnk());
							newUser.setPortfolio_link(signUpRequest.getPortfolio_link());
							newUser.setBlogs_link(signUpRequest.getBlogs_link());
							newUser.setModified_by(currentuser.getUser_id());
							String strRoles = signUpRequest.getRole();
							UserRole appRole;

							if (strRoles == null) {
								appRole = roleRepository.findByName(ERole.ROLE_USER);
							} else {
								switch (strRoles) {
								case "admin":
									appRole = roleRepository.findByName(ERole.ROLE_ADMIN);
									break;
								case "manager":
									appRole = roleRepository.findByName(ERole.ROLE_MANAGER);
									break;
								default:
									appRole = roleRepository.findByName(ERole.ROLE_USER);
//									return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//							                .body(new MessageResponse("Application role not valid."));
								}
							}
							newUser.setAppRole(appRole);
							String encodedPassword = passwordEncoder.encode(newUser.getPassword());
							newUser.setPassword(encodedPassword);
							newUser.set_deleted(false); // Mark the user as not soft-deleted

							User user = userRepository.save(newUser);
							
							// Save the user-role relationship
							UserRolesMapping userRolesMapping = new UserRolesMapping(user, currentRole);
							usereRolesMappingRepository.save(userRolesMapping);

							for (Long id : signUpRequest.getManagerIds()) {
								User manager = userRepository.findById(id).get();
								System.out.println("manager id - " + manager);
								ReportingManager reportingManager = new ReportingManager();
								reportingManager.setEmployee(user);
								reportingManager.setManager(manager);
								reportingManagerRepository.save(reportingManager);
							}

							// Send the email with the generated password
							sendEmailPassword(newUser, newUser.getPassword());
						} else {
							return ResponseEntity.status(HttpStatus.OK)
									.body(new MessageResponse("Employee data added successfully."));
						}
//					}else {
//						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//								.body(new MessageResponse("Current role does not exists."));
//					}
					} else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new MessageResponse("Employee ID or email id already exists."));
					}
					return ResponseEntity.status(HttpStatus.OK)
							.body(new MessageResponse("Employee data added successfully."));
				} else {
					// User with the same email already exists and is not soft-deleted
					throw new UserNotFoundException("Employee is already exists.");
				}
			} else {
				
//				List<Roles> allRoles = rolesRepository.findAll();
//                Roles currentRole = allRoles.stream()
//                        .filter(role -> role.getRole_name().equals(signUpRequest.getCurrent_role()))
//                        .findFirst()
//                        .orElse(null);
//                if (currentRole != null) {

				// Check if the employee_id is unique
				boolean usersWithDuplicateEmployeeId = userRepository
						.existsByEmployeeId(signUpRequest.getEmployee_Id());
				boolean userWithDuplicateEmailId = userRepository.existsByEmail(signUpRequest.getEmail());
				boolean existBySoftDelete = userRepository
						.existsByEmployeeIdAndNotDeleted(signUpRequest.getEmployee_Id());

				if (!usersWithDuplicateEmployeeId && !userWithDuplicateEmailId) {
					if (!existBySoftDelete) {						
					// Create a new user
					String password = generateRandomPassword();
					User newUser = new User(signUpRequest.getEmail(), password);
					newUser.setFull_name(signUpRequest.getFull_name());
					newUser.setEmployee_Id(signUpRequest.getEmployee_Id());
					newUser.setCurrent_role(signUpRequest.getCurrent_role());
					newUser.setUser_image(signUpRequest.getUser_image());
					newUser.setGender(signUpRequest.getGender());
					newUser.setMobile_number(signUpRequest.getMobile_number());
					newUser.setLocation(signUpRequest.getLocation());
					newUser.setDate_of_joining(signUpRequest.getDate_of_joining());
					newUser.setDate_of_birth(signUpRequest.getDate_of_birth());
					newUser.setLinkedin_lnk(signUpRequest.getLinkedin_lnk());
					newUser.setPortfolio_link(signUpRequest.getPortfolio_link());
					newUser.setBlogs_link(signUpRequest.getBlogs_link());
					newUser.setModified_by(currentuser.getUser_id());

					String strRoles = signUpRequest.getRole();
					UserRole appRole;

					if (strRoles == null) {
						appRole = roleRepository.findByName(ERole.ROLE_USER);
					} else {
						switch (strRoles) {
						case "admin":
							appRole = roleRepository.findByName(ERole.ROLE_ADMIN);
							break;
						case "manager":
							appRole = roleRepository.findByName(ERole.ROLE_MANAGER);
							break;
						default:
							appRole = roleRepository.findByName(ERole.ROLE_USER);
//							return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					                .body(new MessageResponse("Application role not valid."));
						}
					}

					newUser.setAppRole(appRole);
					String encodedPassword = passwordEncoder.encode(newUser.getPassword());
					newUser.setPassword(encodedPassword);

					User user = userRepository.save(newUser);
					
					// Save the user-role relationship
					UserRolesMapping userRolesMapping = new UserRolesMapping(user, currentRole);
					usereRolesMappingRepository.save(userRolesMapping);

					for (Long id : signUpRequest.getManagerIds()) {
						User manager = userRepository.findById(id).get();
						ReportingManager reportingManager = new ReportingManager();
						reportingManager.setEmployee(user);
						reportingManager.setManager(manager);
						reportingManagerRepository.save(reportingManager);
					}

					// Send the email with the generated password
					sendEmailPassword(newUser, password);

					  // Activity history logic
		            
		            UserToJsonConverter userToJsonConverter = new UserToJsonConverter();
					
//					 String activityType = "Add Employee";
//				     String description = "New Employee Added";
//				    
//				     activityHistoryService.addActivity(activityType, description, newData, null, currentuser.getFull_name());
		            
//		            ActivityHistory activityHistory = new ActivityHistory();
//		            activityHistory.setActivity_type("Add User");
//		            activityHistory.setDescription("New User added");
//		            String newData = userToJsonConverter.convertUserToJSON(newUser);
//		            activityHistory.setNew_data(newData);
//		            activityHistoryService.addActivity(activityHistory, principal);
				
				} else {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new MessageResponse("Employee data added successfully."));
				}
//				}else {
//					return ResponseEntity.status(HttpStatus.OK)
//							.body(new MessageResponse("Current role does not exists."));
//				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new MessageResponse("Employee ID or email id already exists."));
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body(new MessageResponse("Employee data added successfully."));
		} 
			
		} catch (Exception e) {
			logger.error("Error adding a new user.", e);
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
					.body(new MessageResponse("Employee with same email id and employee id already exist"));
		}

	}
	
	
	
	public ResponseEntity<?> editEmployee(Long userId, UserDto editUserRequest, Principal principal) {
	    logger.info("Editing user details.");

	    try {
	    	User currentuser = userRepository.findByEmailId(principal.getName());
	        // Retrieve the user to be updated
	        Optional<User> optionalUser = userRepository.findById(userId);
	        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User not found"));

	        // Update the user details
	        user.setFull_name(editUserRequest.getFull_name());
	        //user.setEmployee_Id(editUserRequest.getEmployee_Id());
	        user.setCurrent_role(editUserRequest.getCurrent_role());
	        //user.setEmail(editUserRequest.getEmail());
	        user.setUser_image(editUserRequest.getUser_image());
	        user.setGender(editUserRequest.getGender());
	        user.setMobile_number(editUserRequest.getMobile_number());
	        user.setLocation(editUserRequest.getLocation());
	        user.setDate_of_joining(editUserRequest.getDate_of_joining());
	        user.setDate_of_birth(editUserRequest.getDate_of_birth());
	        user.setLinkedin_lnk(editUserRequest.getLinkedin_lnk());
	        user.setPortfolio_link(editUserRequest.getPortfolio_link());
	        user.setBlogs_link(editUserRequest.getBlogs_link());
	        user.setModified_on(LocalDateTime.now());
	        user.setModified_by(currentuser.getUser_id());

	        // Check if the provided current_role exists in the roles table
	        String currentRoleName = editUserRequest.getCurrent_role();
	        Roles currentRole = rolesRepository.findByRoleName(currentRoleName);
	        if (currentRole == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(new MessageResponse("Current role does not exist."));
	        }

	        // Save the updated user
	        User updatedUser = userRepository.save(user);

	        // Save the user-role relationship
	        UserRolesMapping userRolesMapping = new UserRolesMapping(updatedUser, currentRole);
	        usereRolesMappingRepository.save(userRolesMapping);

	        // Update reporting managers (you may need to adjust this based on your requirements)
	        reportingManagerRepository.deleteByEmployee(user);

	        for (Long id : editUserRequest.getManagerIds()) {
	            User manager = userRepository.findById(id)
	                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

	            ReportingManager reportingManager = new ReportingManager();
	            reportingManager.setEmployee(updatedUser);
	            reportingManager.setManager(manager);
	            reportingManagerRepository.save(reportingManager);
	        }
	        
	     // Compare the fields and identify changes
	        Map<String, String> changes = new HashMap<>();
	        if (!Objects.nullSafeEquals(user.getFull_name(), updatedUser.getFull_name())) {
	            changes.put("full_name", updatedUser.getFull_name());
	        }
	        if (!Objects.nullSafeEquals(user.getEmployee_Id(), updatedUser.getEmployee_Id())) {
	            changes.put("employee_Id", updatedUser.getEmployee_Id());
	        }
	        if (!Objects.nullSafeEquals(user.getCurrent_role(), updatedUser.getCurrent_role())) {
	            changes.put("current_role", updatedUser.getCurrent_role());
	        }
	        if (!Objects.nullSafeEquals(user.getUser_image(), updatedUser.getUser_image())) {
	            changes.put("user_image", updatedUser.getUser_image());
	        }
	        if (!Objects.nullSafeEquals(user.getGender(), updatedUser.getGender())) {
	            changes.put("gender", updatedUser.getGender());
	        }
	        if (!Objects.nullSafeEquals(user.getMobile_number(), updatedUser.getMobile_number())) {
	            changes.put("mobile_number", updatedUser.getMobile_number());
	        }
	        if (!Objects.nullSafeEquals(user.getLocation(), updatedUser.getLocation())) {
	            changes.put("location", updatedUser.getLocation());
	        }
	        if (!Objects.nullSafeEquals(user.getDate_of_joining(), updatedUser.getDate_of_joining())) {
	            changes.put("date_of_joining", updatedUser.getDate_of_joining());
	        }
	        if (!Objects.nullSafeEquals(user.getDate_of_birth(), updatedUser.getDate_of_birth())) {
	            changes.put("date_of_birth", updatedUser.getDate_of_birth());
	        }
	        if (!Objects.nullSafeEquals(user.getLinkedin_lnk(), updatedUser.getLinkedin_lnk())) {
	            changes.put("linkedin_lnk", updatedUser.getLinkedin_lnk());
	        }
	        if (!Objects.nullSafeEquals(user.getPortfolio_link(), updatedUser.getPortfolio_link())) {
	            changes.put("portfolio_link", updatedUser.getPortfolio_link());
	        }
	        if (!Objects.nullSafeEquals(user.getBlogs_link(), updatedUser.getBlogs_link())) {
	            changes.put("blogs_link", updatedUser.getBlogs_link());
	        }


//	        UserToJsonConverter userToJsonConverter = new UserToJsonConverter();
//
//			try {
//				String newData = userToJsonConverter.convertChangesToJson(changes);
//				String oldData = userToJsonConverter.convertUserToJSON(user);
//
//				ActivityHistory activityHistory = new ActivityHistory();
//	            activityHistory.setActivity_type("Update Employee");
//	            activityHistory.setDescription("Change in employee data");
//	            activityHistory.setOld_data(oldData);
//	            activityHistory.setNew_data(newData);
//	            activityHistory.setUser(user);
//	            activityHistoryService.addActivity(activityHistory, principal);
//				
//			} catch (JsonProcessingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

	        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee details edit successfully.."));
	    } catch (Exception e) {
	        logger.error("Employee details edit successfully.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new MessageResponse("Employee details edit successfully."));
	    }
	}

	
	
//	public ResponseEntity<?> addUser(SignupRequest signUpRequest, Principal principal)throws UserNotFoundException {
//		 try {
//			 
//			 User currentuser = userRepository.findByEmailId(principal.getName());
//			 
//			 if (currentuser == null) {
//		            throw new UserNotFoundException("Current user not found.");
//		        }
//
//			 String password = generateRandomPassword();
//			 
//			 User user = new User(signUpRequest.getEmail(), generateRandomPassword());
//		        user.setFull_name(signUpRequest.getFull_name());
//		        user.setEmployee_Id(signUpRequest.getEmployee_Id());
//		        user.setCurrent_role(signUpRequest.getCurrent_role());
//		        user.setUser_image(signUpRequest.getUser_image());
//		        user.setGender(signUpRequest.getGender());
//		        user.setMobile_number(signUpRequest.getMobile_number());
//		        user.setLocation(signUpRequest.getLocation());
//		        user.setDate_of_joining(signUpRequest.getDate_of_joining());
//		        user.setDate_of_birth(signUpRequest.getDate_of_birth());
//		        user.setLinkedin_lnk(signUpRequest.getLinkedin_lnk());
//		        user.setPortfolio_link(signUpRequest.getPortfolio_link());
//		        user.setBlogs_link(signUpRequest.getBlogs_link());
//		        user.setModified_by(currentuser.getFull_name());
//		        String strRoles = signUpRequest.getRole();
//
//		        if (strRoles == null) {
//		            UserRole userRole = roleRepository.findByName(ERole.ROLE_USER);
//		            user.setAppRole(userRole);
//		        } else {
//		            switch (strRoles) {
//		                case "admin":
//		                    UserRole adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
//		                    user.setAppRole(adminRole);
//
//		                    break;
//		                case "manager":
//		                    UserRole managerRole = roleRepository.findByName(ERole.ROLE_MANAGER);
//		                    user.setAppRole(managerRole);
//
//		                    break;
//		                default:
//		                    UserRole userRole = roleRepository.findByName(ERole.ROLE_USER);
//		                    user.setAppRole(userRole);
//
//		            }
//		        }
//		        String encodedPassword = passwordEncoder.encode(user.getPassword());
//		        user.setPassword(encodedPassword);
//
//		        userRepository.save(user);
//
//		        // Send the email with the generated password
//		        sendEmailPassword(user, password);
//
//		        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee data added successfully."));
//		    } catch (Exception e) {
//		        throw new UserNotFoundException("Failed to add user data." + e.getMessage());
//		    } 
//	}
    
	/**
     * Edit an existing user.
     *
     * @param userId      User ID of the user to edit.
     * @param updatedUser User object containing updated details.
     * @param principal   Principal representing the authenticated user.
     * @return Updated User entity.
     */
	
	// Update the existing user
    @Override
	public User editUser(Long userId, User updatedUser, Principal principal) {
    	User currentuser = userRepository.findByEmailId(principal.getName());
    	// Check if the user exists
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));	
      

        // Check if the user is soft-deleted
        if (existingUser.is_deleted()) {
            throw new UserNotFoundException("User does not exist.");
        }
        
       
     // Update fields if the new value is not null
        if (updatedUser.getDate_of_birth() != null) {
            existingUser.setDate_of_birth(updatedUser.getDate_of_birth());
        }
        if (updatedUser.getGender() != null) {
            existingUser.setGender(updatedUser.getGender());
        }
        if (updatedUser.getLocation() != null) {
            existingUser.setLocation(updatedUser.getLocation());
        }
        if (updatedUser.getProfessional_summary() != null) {
            existingUser.setProfessional_summary(updatedUser.getProfessional_summary());
        }
        if (updatedUser.getUser_image() != null) {
            existingUser.setUser_image(updatedUser.getUser_image());
        }
        if (updatedUser.getMobile_number() != null) {
            existingUser.setMobile_number(updatedUser.getMobile_number());
        }
        if (updatedUser.getLinkedin_lnk() != null) {
            existingUser.setLinkedin_lnk(updatedUser.getLinkedin_lnk());
        }
        if (updatedUser.getPortfolio_link() != null) {
            existingUser.setPortfolio_link(updatedUser.getPortfolio_link());
        }
        if (updatedUser.getBlogs_link() != null) {
            existingUser.setBlogs_link(updatedUser.getBlogs_link());
        }

        existingUser.setModified_by(currentuser.getUser_id());
        
        
        // Compare the fields and identify changes
        Map<String, String> changes = new HashMap<>();
        if (!Objects.nullSafeEquals(existingUser.getFull_name(), updatedUser.getFull_name())) {
            changes.put("full_name", updatedUser.getFull_name());
        }
        if (!Objects.nullSafeEquals(existingUser.getEmail(), updatedUser.getEmail())) {
            changes.put("email", updatedUser.getEmail());
        }
        if (!Objects.nullSafeEquals(existingUser.getDate_of_birth(), updatedUser.getDate_of_birth())) {
            changes.put("Date of Birth", updatedUser.getDate_of_birth());
        }
        if (!Objects.nullSafeEquals(existingUser.getGender(), updatedUser.getGender())) {
            changes.put("Gender", updatedUser.getGender());
        }
        if (!Objects.nullSafeEquals(existingUser.getLocation(), updatedUser.getLocation())) {
            changes.put("Location", updatedUser.getLocation());
        }
        
        System.out.println("changes for user"+changes);
        
        	
         String activityType = "Update Employee";
	     String description = "Change in Employee Data";
	     
	     UserToJsonConverter userToJsonConverter = new UserToJsonConverter();
	           
		try {
			 String newData = userToJsonConverter.convertChangesToJson(changes);
			 String oldData = userToJsonConverter.convertUserToJSON(existingUser);
			 
			 activityHistoryService.
			    addActivity
			    (activityType, description,newData ,oldData, currentuser.getFull_name());
			}
		catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return userRepository.save(existingUser);
	}

    /**
     * Get a user by their email.
     *
     * @param email Email address of the user.
     * @return The User entity if found, or null if not found.
     */

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmailId(email);
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
                + "We have generated a login credential. Please use the following username and password to login QW Resume Builder:<br>"
                +"Username: " +user.getEmail()+ "<br>"
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

	/**
     * Delete a user by their user ID.
     *
     * @param userId    User ID of the user to delete.
     * @param principal Principal representing the authenticated user.
     */
	
	@Override
	public void deleteUserById(Long userId, Principal principal) {
		User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));  
		
		 String activityType = "Delete Employee";
	     String description = "Change in Employee Data";
	     
	    activityHistoryService.addActivity(activityType, description,"user with"+userId + "deleted", null, principal.getName());
		
        userRepository.delete(existingUser);
		
	}
	

	public boolean checkUserExists(String UserId) {
		boolean result=false;
		result=userRepository.existsById(Long.parseLong(UserId));
		return result;
	}

//	@Override
//	public List<User> getManagers() {
//        String roleName = "ROLE_MANAGER"; // The appRole filter
//        List<User> managers = userRepository.findByAppRoleName(roleName);
//        return managers;
//    }

    
}

