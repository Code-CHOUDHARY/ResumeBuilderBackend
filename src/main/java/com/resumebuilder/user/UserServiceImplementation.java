package com.resumebuilder.user;

import java.security.Principal;
import java.security.SecureRandom;

import java.util.List;

import java.util.Optional;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.resumebuilder.auth.SignupRequest;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.reportingmanager.ReportingManager;
import com.resumebuilder.reportingmanager.ReportingManagerRepository;
import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.approle.UserRole;
import com.resumebuilder.security.response.MessageResponse;


@Service
public class UserServiceImplementation implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired
	    private UserRoleRepository roleRepository;
	 
	 @Autowired
	 private JavaMailSender mailSender;
	 
	 @Autowired
	 private ReportingManagerRepository reportingManagerRepository;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;

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
	@Override
	public ResponseEntity<?> addUser(SignupRequest signUpRequest, Principal principal) throws UserNotFoundException {

		try {
	        User currentuser = userRepository.findByEmailId(principal.getName());	        
	        if (currentuser == null) {
	            throw new UserNotFoundException("Current user not found.");
	        }
	        // Check if a user with the same email exists (soft-deleted or not)
	        User existingUser = userRepository.findByEmailId(signUpRequest.getEmail());

	        if (existingUser != null) {
	            // If an existing user with the same email exists
	            if (existingUser.is_deleted()) {
	                // If it's soft-deleted, create a new user without overwriting the existing soft-deleted user
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
	                    }
	                }

	                newUser.setAppRole(appRole);
	                String encodedPassword = passwordEncoder.encode(newUser.getPassword());
	                newUser.setPassword(encodedPassword);
	                newUser.set_deleted(false); // Mark the user as not soft-deleted
	                User user = userRepository.save(newUser);
	                for(Long id: signUpRequest.getManagerIds()) {	           	                	
	                	User manager = userRepository.findById(id).get();	                	
	                	System.out.println("manager id - "+manager);
	                	 ReportingManager reportingManager = new ReportingManager();	      
	                	 reportingManager.setEmployee(user);
	                	 reportingManager.setManager(manager);
	                	 reportingManagerRepository.save(reportingManager);	                		                	 
	                }
	               	                	                
	                // Send the email with the generated password
	                sendEmailPassword(newUser, newUser.getPassword());

	                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee data added successfully."));
	            } else {
	                // User with the same email already exists and is not soft-deleted
	                throw new UserNotFoundException("User with this email already exists.");
	            }
	        } else {
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
	                }
	            }

	            newUser.setAppRole(appRole);
	            String encodedPassword = passwordEncoder.encode(newUser.getPassword());
	            newUser.setPassword(encodedPassword);

	            User user = userRepository.save(newUser);
                for(Long id: signUpRequest.getManagerIds()) {             	
                	User manager = userRepository.findById(id).get();
                	 ReportingManager reportingManager = new ReportingManager();      
                	 reportingManager.setEmployee(user);
                	 reportingManager.setManager(manager);
                	 reportingManagerRepository.save(reportingManager);              	 
                }

	            // Send the email with the generated password
	            sendEmailPassword(newUser, password);

	            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee data added successfully."));
	        }
	    } catch (Exception e) {
	        throw new UserNotFoundException("Failed to add/update user data. " + e.getMessage());
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
        
        existingUser.setFull_name(updatedUser.getFull_name());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDate_of_birth(updatedUser.getDate_of_birth());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setLocation(updatedUser.getLocation());
        existingUser.setCurrent_role(updatedUser.getCurrent_role());
        existingUser.setEmployee_Id(updatedUser.getEmployee_Id());
        existingUser.setAppRole(updatedUser.getAppRole());
        existingUser.setUser_image(updatedUser.getUser_image());
        existingUser.setMobile_number(updatedUser.getMobile_number());
        existingUser.setDate_of_joining(updatedUser.getDate_of_joining());
        existingUser.setLinkedin_lnk(updatedUser.getLinkedin_lnk());
        existingUser.setPortfolio_link(updatedUser.getPortfolio_link());
        existingUser.setBlogs_link(updatedUser.getBlogs_link());
        existingUser.setModified_by(currentuser.getUser_id());
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
        userRepository.delete(existingUser);
		
	}
	
//	@Override
//	public List<User> getManagers() {
//        String roleName = "ROLE_MANAGER"; // The appRole filter
//        List<User> managers = userRepository.findByAppRoleName(roleName);
//        return managers;
//    }
    
}

