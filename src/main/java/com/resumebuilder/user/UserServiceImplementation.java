package com.resumebuilder.user;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.catalina.Manager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.resumebuilder.DTO.UserDto;
import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryRepository;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.auth.SignupRequest;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.reportingmanager.ReportingManager;
import com.resumebuilder.reportingmanager.ReportingManagerRepository;
import com.resumebuilder.reportingmanager.ReportingManagerService;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;
import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.approle.UserRole;
import com.resumebuilder.security.response.MessageResponse;

import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.lang.Objects;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImplementation implements UserService {

	private final static Logger logger = LogManager.getLogger(UserServiceImplementation.class);

	private final UserRepository userRepository;

	private final RolesRepository rolesRepository;

	private final UserRolesMappingRepository usereRolesMappingRepository;

	private final UserService userService;

	private final UserRoleRepository roleRepository;

	private final JavaMailSender mailSender;

	private final ReportingManagerRepository reportingManagerRepository;

	private final PasswordEncoder passwordEncoder;

	private final ActivityHistoryService activityHistoryService;

	private final ActivityHistoryRepository activityHistoryRepository;
	
	private final ReportingManagerService reportingManagerService;
@Autowired
	private ModelMapper mapper;
@Autowired
private ProfileImageService profileService;
	@Autowired
	    public UserServiceImplementation(@Lazy UserRepository userRepository,@Lazy RolesRepository rolesRepository,@Lazy UserRolesMappingRepository usereRolesMappingRepository,@Lazy UserService userService,
	    		@Lazy UserRoleRepository roleRepository, @Lazy JavaMailSender mailSender,@Lazy ReportingManagerRepository reportingManagerRepository, PasswordEncoder passwordEncoder, ActivityHistoryService activityHistoryService, 
	    		ActivityHistoryRepository activityHistoryRepository,@Lazy ReportingManagerService reportingManagerService) {
	    this.userRepository=userRepository;    
	    this.rolesRepository=rolesRepository;
	    this.usereRolesMappingRepository=usereRolesMappingRepository;
		this.userService = userService;
		this.roleRepository=roleRepository;
		this.mailSender=mailSender;
		this.reportingManagerRepository=reportingManagerRepository;
		this.passwordEncoder=passwordEncoder;
		this.activityHistoryService=activityHistoryService;
		this.activityHistoryRepository=activityHistoryRepository;
		this.reportingManagerService=reportingManagerService;
	    }
	 /**
	     * Retrieve a list of all users.
	     *
	     * @return List of User entities.
	     */
	 
		 @Value("${login.url}")
		    private String loginUrl;
			
		
			public List<UserDto> getAllUsers() {
				List<User> usersList = userRepository.getAllActiveUsers();
				// Convert Role entities to RoleDto objects
				List<UserDto> dtoList = usersList.stream().map(this::convertToDto).collect(Collectors.toList());

				return dtoList;
			}

			private UserDto convertToDto(User user) {
				UserDto userDto = new UserDto();
				userDto.setUser_id(user.getUser_id());
				userDto.setFull_name(user.getFull_name());
				userDto.setCurrent_role(user.getCurrent_role());
				userDto.setModified_on(user.getModified_on());
				userDto.setModified_by(userService.findUserByIdUser(user.getModified_by()).getFull_name());
				return userDto;
			}

	/**
	 * Find a user by their user ID.
	 *
	 * @param userId User ID.
	 * @return The User entity if found, or throw UserNotFoundException.
	 */

	public User findUserByIdUser(Long userId) {
		logger.info("Finding user by ID: {}", userId);
		Optional<User> opt = userRepository.findById(userId);
		logger.info(opt.get().getUser_id() + "user");
		return opt.get();
	}

	/**
	 * Find a user by their username (email).
	 *
	 * @param userName Username (email).
	 * @return The User entity if found, or throw UserNotFoundException.
	 * @throws java.io.IOException 
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */

	@Override
	public UserDto findUserByUsername(String userName) throws IOException, FileNotFoundException, java.io.IOException {
		logger.info("Finding user by username: {}", userName);
		Optional<User> opt = userRepository.findByEmail(userName);
		UserDto user=mapper.map(opt.get(), UserDto.class);
		
		byte[] imageData = profileService.getProfileImageByUserId(opt.get().getUser_id());
		//System.out.println("imagedata"+imageData);
		if(imageData!=null) {
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
		 String base64Image = convertImageToBase64(image);
       
		 user.setImage(base64Image);
		}
		return user;
	}
	private String convertImageToBase64(BufferedImage image) throws java.io.IOException {
        // Create a ByteArrayOutputStream to write the image data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            // Write the image data to the ByteArrayOutputStream
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        // Convert the byte array to a Base64-encoded string
        byte[] imageData = baos.toByteArray();
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageData);

        return base64EncodedImage;
    }

	/**
     * Add a new user or update an existing one.
     *
     * @param signUpRequest SignupRequest containing user details.
     * @param principal      Principal representing the authenticated user.
     * @return ResponseEntity with a success message or error.
     * @throws UserNotFoundException if adding or updating the user fails.
     */
	
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
							newUser.setTechnology_stack(signUpRequest.getTechnology_stack());
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

//							for (Long id : signUpRequest.getManagerIds()) {
//								User manager = userRepository.findById(id).get();
//								System.out.println("manager id - " + manager);
//								ReportingManager reportingManager = new ReportingManager();
//								reportingManager.setEmployee(user);
//								reportingManager.setManager(manager);
//								reportingManagerRepository.save(reportingManager);
//							}
							
							// Check if managerIds are provided before iterating over them
					        if (signUpRequest.getManagerIds() != null) {
					            for (Long id : signUpRequest.getManagerIds()) {
					                User manager = userRepository.findById(id).orElse(null);
					                if (manager != null) {
					                    ReportingManager reportingManager = new ReportingManager();
					                    reportingManager.setEmployee(user);
					                    reportingManager.setManager(manager);
					                    reportingManagerRepository.save(reportingManager);
					                }
					            }
					        }

							// Send the email with the generated password
							sendEmailPassword(newUser, newUser.getPassword());
						} else {
							return ResponseEntity.status(HttpStatus.OK)
									.body(new MessageResponse("Employee data added successfully."));
						}

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
					newUser.setTechnology_stack(signUpRequest.getTechnology_stack());
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
						}
					}

					newUser.setAppRole(appRole);
					String encodedPassword = passwordEncoder.encode(newUser.getPassword());
					newUser.setPassword(encodedPassword);

					User user = userRepository.save(newUser);
					
					// Save the user-role relationship
					UserRolesMapping userRolesMapping = new UserRolesMapping(user, currentRole);
					usereRolesMappingRepository.save(userRolesMapping);

//					for (Long id : signUpRequest.getManagerIds()) {
//						User manager = userRepository.findById(id).get();
//						ReportingManager reportingManager = new ReportingManager();
//						reportingManager.setEmployee(user);
//						reportingManager.setManager(manager);
//						reportingManagerRepository.save(reportingManager);
//					}
					
					// Check if managerIds are provided before iterating over them
			        if (signUpRequest.getManagerIds() != null) {
			            for (Long id : signUpRequest.getManagerIds()) {
			                User manager = userRepository.findById(id).orElse(null);
			                if (manager != null) {
			                    ReportingManager reportingManager = new ReportingManager();
			                    reportingManager.setEmployee(user);
			                    reportingManager.setManager(manager);
			                    reportingManagerRepository.save(reportingManager);
			                }
			            }
			        }

					// Send the email with the generated password
					sendEmailPassword(newUser, password);

					  // Activity history logic
		            
		            UserToJsonConverter userToJsonConverter = new UserToJsonConverter();
		            
		            ActivityHistory activityHistory = new ActivityHistory();
		            activityHistory.setActivity_type("Add User");
		            activityHistory.setDescription("New User added");
		            String newData = userToJsonConverter.convertUserToJSON(newUser);		      
		            activityHistory.setNew_data(newData);
		            activityHistoryService.addActivity(activityHistory, principal);
				
				} else {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new MessageResponse("Employee data added successfully."));
				}
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
	
	//edit employee details
	@Transactional
	public ResponseEntity<?> editEmployee(Long userId, UserDto editUserRequest, Principal principal) {
	    logger.info("Editing user details.");

	    try {
	    	User currentuser = userRepository.findByEmailId(principal.getName());
	        // Retrieve the user to be updated
	        Optional<User> optionalUser = userRepository.findById(userId);
	        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User not found"));

	        // Update the user details
	        //user.setFull_name(editUserRequest.getFull_name());
	        //user.setCurrent_role(editUserRequest.getCurrent_role());
	       // user.setUser_image(editUserRequest.getUser_image());
	        user.setGender(editUserRequest.getGender());
	        user.setEmail(editUserRequest.getEmail());
	        user.setEmployee_Id(editUserRequest.getEmployee_Id());
	        user.setMobile_number(editUserRequest.getMobile_number());
	        user.setLocation(editUserRequest.getLocation());
	        //user.setDate_of_joining(editUserRequest.getDate_of_joining());
	        user.setDate_of_birth(editUserRequest.getDate_of_birth());
//	        user.setLinkedin_lnk(editUserRequest.getLinkedin_lnk());
//	        user.setPortfolio_link(editUserRequest.getPortfolio_link());
//	        user.setBlogs_link(editUserRequest.getBlogs_link());
	        user.setModified_on(LocalDateTime.now());
	        user.setModified_by(currentuser.getUser_id());

	        // Check if the provided current_role exists in the roles table
	        String currentRoleName = editUserRequest.getCurrent_role();
	        Roles currentRole = rolesRepository.findByRoleName(currentRoleName);
	        if (currentRole == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(new MessageResponse("Current role does not exist."));
	        }
	        
	        // Activity history for edit user	        
	        // Compare the fields and identify changes
	        
	        Map<String, String> changes = new HashMap<>();
	        
	        if (!Objects.nullSafeEquals(user.getEmployee_Id(), editUserRequest.getEmployee_Id())) {
	            changes.put("employee_Id", editUserRequest.getEmployee_Id());
	        }
	        
	        if (!Objects.nullSafeEquals(user.getGender(), editUserRequest.getGender())) {
	            changes.put("gender", editUserRequest.getGender());
	        }
	        if (!Objects.nullSafeEquals(user.getMobile_number(), editUserRequest.getMobile_number())) {
	            changes.put("mobile_number", editUserRequest.getMobile_number());
	        }
	        if (!Objects.nullSafeEquals(user.getLocation(), editUserRequest.getLocation())) {
	            changes.put("location", editUserRequest.getLocation());
	        }
	        if (!Objects.nullSafeEquals(user.getDate_of_birth(), editUserRequest.getDate_of_birth())) {
	            changes.put("date_of_birth", editUserRequest.getDate_of_birth());
	        }
	        


	        UserToJsonConverter userToJsonConverter = new UserToJsonConverter();

			try {
				String newData = userToJsonConverter.convertChangesToJson(changes);
				String oldData = userToJsonConverter.convertUserToJSON(user);

				ActivityHistory activityHistory = new ActivityHistory();
	            activityHistory.setActivity_type("Update Employee");
	            activityHistory.setDescription("Change in employee data");
	            activityHistory.setOld_data(oldData);
	            activityHistory.setNew_data(newData);
	            activityHistory.setUser(user);
	            activityHistoryService.addActivity(activityHistory, principal);
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        // Save the updated user
	        User updatedUser = userRepository.save(user);

	        // Save the user-role relationship
	        UserRolesMapping userRolesMapping = new UserRolesMapping(updatedUser, currentRole);
	        usereRolesMappingRepository.save(userRolesMapping);	        

//	     // Update reporting managers
//	        List<Long> managerIds = editUserRequest.getManagerIds();
//	        if (managerIds != null) {
//	            // Clear existing reporting managers
//	            reportingManagerRepository.deleteByEmployee(updatedUser);
//
//	            // Add new reporting managers
//	            for (Long managerId : managerIds) {
//	                User manager = userRepository.findById(managerId)
//	                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + managerId));
//
//	                ReportingManager reportingManager = new ReportingManager();
//	                reportingManager.setEmployee(updatedUser);
//	                reportingManager.setManager(manager);
//	                reportingManagerRepository.save(reportingManager);
//	            }
//	        }
	        
	     // Retrieve existing reporting managers
	        List<ReportingManager> existingManagers = reportingManagerRepository.findByEmployee(updatedUser);
	     // Update or add new reporting managers
	        List<Long> managerIds = editUserRequest.getManagerIds();
	        if (managerIds != null) {
	            for (Long managerId : managerIds) {
	                User manager = userRepository.findById(managerId)
	                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + managerId));

	                // Check if a reporting manager already exists for this manager
	                ReportingManager existingManager = findExistingManager(existingManagers, manager);

	                if (existingManager != null) {
	                    // Update the existing reporting manager
	                    existingManager.setManager(manager);
	                    reportingManagerRepository.save(existingManager);
	                } else {
	                    // If not exists, create a new reporting manager
	                    ReportingManager reportingManager = new ReportingManager();
	                    reportingManager.setEmployee(updatedUser);
	                    reportingManager.setManager(manager);
	                    reportingManagerRepository.save(reportingManager);
	                }
	            }

	            // Remove reporting managers that are not in the new list
	            removeManagersNotInList(existingManagers, managerIds, updatedUser);
	        }
	        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee details edit successfully.."));
	    } catch (Exception e) {
	        logger.error("Employee details edit successfully.", e);
	        return ResponseEntity.status(HttpStatus.OK)
	                .body(new MessageResponse("Employee details edit successfully."));
	    }
	}

	// Helper method to find an existing reporting manager for a given manager
	private ReportingManager findExistingManager(List<ReportingManager> existingManagers, User manager) {
	    for (ReportingManager existingManager : existingManagers) {
	        if (existingManager.getManager().equals(manager)) {
	            return existingManager;
	        }
	    }
	    return null;
	}

	private void removeManagersNotInList(List<ReportingManager> existingManagers, List<Long> managerIds, User updatedUser) {
	    List<ReportingManager> managersToRemove = new ArrayList<>();

	    // Identify reporting managers that are not in the new list
	    for (ReportingManager existingManager : existingManagers) {
	        if (!managerIds.contains(existingManager.getManager().getUser_id())) {
	            managersToRemove.add(existingManager);
	        }
	    }

	    // Remove reporting managers that are not in the new list
	    for (ReportingManager managerToRemove : managersToRemove) {
	        existingManagers.remove(managerToRemove);
	        reportingManagerRepository.delete(managerToRemove);
	    }
	}




	// Update the user personal details
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
        
       
//     // Update fields if the new value is not null
//        if (updatedUser.getDate_of_birth() != null) {
//            existingUser.setDate_of_birth(updatedUser.getDate_of_birth());
//        }
//        if (updatedUser.getGender() != null) {
//            existingUser.setGender(updatedUser.getGender());
//        }
//        if (updatedUser.getLocation() != null) {
//            existingUser.setLocation(updatedUser.getLocation());
//        }
//        if (updatedUser.getProfessional_summary() != null) {
//            existingUser.setProfessional_summary(updatedUser.getProfessional_summary());
//        }
//        if (updatedUser.getUser_image() != null) {
//            existingUser.setUser_image(updatedUser.getUser_image());
//        }
//        if (updatedUser.getMobile_number() != null) {
//            existingUser.setMobile_number(updatedUser.getMobile_number());
//        }
//        if (updatedUser.getLinkedin_lnk() != null) {
//            existingUser.setLinkedin_lnk(updatedUser.getLinkedin_lnk());
//        }
//        if (updatedUser.getPortfolio_link() != null) {
//            existingUser.setPortfolio_link(updatedUser.getPortfolio_link());
//        }
//        if (updatedUser.getBlogs_link() != null) {
//            existingUser.setBlogs_link(updatedUser.getBlogs_link());
//        }

        
     // Update fields if the new value is not null or empty
        if (isNotNullOrEmpty(updatedUser.getDate_of_birth())) {
            existingUser.setDate_of_birth(updatedUser.getDate_of_birth());
        }
        if (isNotNullOrEmpty(updatedUser.getGender())) {
            existingUser.setGender(updatedUser.getGender());
        }
        if (isNotNullOrEmpty(updatedUser.getLocation())) {
            existingUser.setLocation(updatedUser.getLocation());
        }
        if (isNotNullOrEmpty(updatedUser.getProfessional_summary())) {
            existingUser.setProfessional_summary(updatedUser.getProfessional_summary());
        }
        if (isNotNullOrEmpty(updatedUser.getMobile_number())) {
            existingUser.setMobile_number(updatedUser.getMobile_number());
        }
        if (isNotNullOrEmpty(updatedUser.getLinkedin_lnk())) {
            existingUser.setLinkedin_lnk(updatedUser.getLinkedin_lnk());
        }
        if (isNotNullOrEmpty(updatedUser.getBlogs_link())) {
            existingUser.setBlogs_link(updatedUser.getBlogs_link());
        }
        if (isNotNullOrEmpty(updatedUser.getPortfolio_link())) {
            existingUser.setPortfolio_link(updatedUser.getPortfolio_link());
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

			ActivityHistory activityHistory = new ActivityHistory();
            activityHistory.setActivity_type("Update Employee");
            activityHistory.setDescription("Change in employee data");
            activityHistory.setOld_data(oldData);
            activityHistory.setNew_data(newData);
            activityHistory.setUser(existingUser);
            activityHistoryService.addActivity(activityHistory, principal);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userRepository.save(existingUser);
	}
	
	
	private boolean isNotNullOrEmpty(String value) {
	    return value != null && !value.trim().isEmpty();
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
	 * @param user             The user to send the email to.
	 * @param generatePassword The generated password.
	 * @throws Exception If there is an error sending the email.
	 */

	public void sendEmailPassword(User user, String generatePassword) throws Exception {
		
	   
		String senderName = "QW Resume Builder";
		String subject = "Credential details";
		String content = "Dear " + user.getFull_name() + ",<br>"
				+ "We have generated a login credential. Please use the following username and password to login QW Resume Builder:<br>"
				+ "Username: " + user.getEmail() + "<br>" + "New Password: " + generatePassword + "<br>";
		 content += "<h3><a href=\"" + loginUrl + "\"><button style=\"background-color: blue; color: white;\">Login</button><a></h3>";
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
		logger.info("Deleting user with ID: {}", userId);
		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		 ActivityHistory activityHistory = new ActivityHistory();
		 activityHistory.setActivity_type("Delete Employee");
		 activityHistory.setDescription("Change in Employee data");
		 activityHistory.setNew_data("Employee with id "+userId+"is deleted");
		 activityHistory.setUser(existingUser);
		 activityHistoryService.addActivity(activityHistory, principal);

		userRepository.delete(existingUser);

	}

	public boolean checkUserExists(String UserId) {
		logger.info("Checking if user exists with ID: {}", UserId);
		boolean result = false;
		result = userRepository.existsById(Long.parseLong(UserId));
		return result;
	}

//	@Override
//	public List<User> getManagers() {
//        String roleName = "ROLE_MANAGER"; // The appRole filter
//        List<User> managers = userRepository.findByAppRoleName(roleName);
//        return managers;
//    }
	
	public ResponseEntity<?> getUserDetailsById(Long userId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User not found"));

            List<Long> managerIds = getManagerIdsByUser(user);

            UserDto userDto = convertUserToDto(user, managerIds);

            // Additional logic to retrieve and set other details like roles, projects, etc.

            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error fetching user details."));
        }
    }

    private List<Long> getManagerIdsByUser(User user) {
        List<ReportingManager> reportingManagers = reportingManagerRepository.findByEmployee(user);
        return reportingManagers.stream()
                .map(reportingManager -> reportingManager.getManager().getUser_id())
                .collect(Collectors.toList());
    }

	
	private UserDto convertUserToDto(User user, List<Long> managerIds) {
		//List<ReportingManager> reportingManagers = user.getReportingManagers();
	    return UserDto.builder()
	            .user_id(user.getUser_id())
	            .employee_Id(user.getEmployee_Id())
	            .full_name(user.getFull_name())
	            .date_of_joining(user.getDate_of_joining())
	            .date_of_birth(user.getDate_of_birth())
	            .current_role(user.getCurrent_role())
	            .email(user.getEmail())
	            .gender(user.getGender())
	            .mobile_number(user.getMobile_number())
	            .location(user.getLocation())
	            .modified_on(user.getModified_on())
	            .user_image(user.getUser_image())
	            .linkedin_lnk(user.getLinkedin_lnk())
	            .portfolio_link(user.getPortfolio_link())
	            .blogs_link(user.getBlogs_link())
	            .professional_summary(user.getProfessional_summary())
	            .technology_stack(user.getTechnology_stack())
	            .managerIds(managerIds)
	            .appRole(user.getAppRole())
	            .build();
	}


}
