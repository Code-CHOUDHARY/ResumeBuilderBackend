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
import org.springframework.stereotype.Service;
import com.resumebuilder.auth.SignupRequest;
import com.resumebuilder.exception.UserNotFoundException;
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

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User findUserByIdUser(Long userId) {
		
		Optional<User> opt =userRepository.findById(userId);
		
		
			return opt.get();
		

	}

	@Override
	public User findUserByUsername(String userName) {
		
		Optional<User> opt = userRepository.findByEmail(userName);
		return opt.get();
	}

	//add the new user
	@Override
	public ResponseEntity<?> addUser(SignupRequest signUpRequest, Principal principal)throws UserNotFoundException {
		 try {
//			 User currentuser = userRepository.findByEmailId(principal.getName());
//				User saveUser = new User();
//				saveUser.setFull_name(user.getFull_name());
//				saveUser.setEmail(user.getEmail());
//				saveUser.setPassword(user.getPassword());
//				saveUser.setEmployee_Id(user.getEmployee_Id());
//				saveUser.setCurrent_role(user.getCurrent_role());
//				saveUser.setAppRole(user.getAppRole());
//				saveUser.setUser_image(user.getUser_image());
//				saveUser.setGender(user.getGender());
//				saveUser.setMobile_number(user.getMobile_number());
//				saveUser.setLocation(user.getLocation());
//				saveUser.setDate_of_joining(user.getDate_of_joining());
//				saveUser.setDate_of_birth(user.getDate_of_birth());
//				saveUser.setLinkedin_lnk(user.getLinkedin_lnk());
//				saveUser.setPortfolio_link(user.getPortfolio_link());
//				saveUser.setBlogs_link(user.getBlogs_link());
//				saveUser.setModified_by(currentuser.getFull_name());
//				
//				 return userRepository.save(saveUser);
			 
			 User currentuser = userRepository.findByEmailId(principal.getName());
			 
			 if (currentuser == null) {
		            throw new UserNotFoundException("Current user not found.");
		        }

			 User user = new User(signUpRequest.getEmail(), generateRandomPassword());
		        user.setFull_name(signUpRequest.getFull_name());
		        user.setEmployee_Id(signUpRequest.getEmployee_Id());
		        user.setCurrent_role(signUpRequest.getCurrent_role());
		        user.setUser_image(signUpRequest.getUser_image());
		        user.setGender(signUpRequest.getGender());
		        user.setMobile_number(signUpRequest.getMobile_number());
		        user.setLocation(signUpRequest.getLocation());
		        user.setDate_of_joining(signUpRequest.getDate_of_joining());
		        user.setDate_of_birth(signUpRequest.getDate_of_birth());
		        user.setLinkedin_lnk(signUpRequest.getLinkedin_lnk());
		        user.setPortfolio_link(signUpRequest.getPortfolio_link());
		        user.setBlogs_link(signUpRequest.getBlogs_link());
		        user.setModified_by(currentuser.getFull_name());
		        String strRoles = signUpRequest.getRole();

		        if (strRoles == null) {
		            UserRole userRole = roleRepository.findByName(ERole.ROLE_USER);
		            user.setAppRole(userRole);
		        } else {
		            switch (strRoles) {
		                case "admin":
		                    UserRole adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
		                    user.setAppRole(adminRole);

		                    break;
		                case "manager":
		                    UserRole managerRole = roleRepository.findByName(ERole.ROLE_MANAGER);
		                    user.setAppRole(managerRole);

		                    break;
		                default:
		                    UserRole userRole = roleRepository.findByName(ERole.ROLE_USER);
		                    user.setAppRole(userRole);

		            }
		        }

		        userRepository.save(user);
		        
		     // Send the email with the generated password
		        sendEmailPassword(user, user.getPassword());

		        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee data added successfully."));
				
			} catch (Exception e) {
				throw new UserNotFoundException("Failed to add user data."+e.getMessage());
			}     
	}
    
	
 // Update the existing user
    @Override
	public User editUser(Long userId, User updatedUser, Principal principal) {
    	User currentuser = userRepository.findByEmailId(principal.getName());
    	User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
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
        existingUser.setModified_by(currentuser.getFull_name());
        return userRepository.save(existingUser);
	}

    //delete the user 
	@Override
	public void deleteUserById(Long userId) {
		User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));     
        userRepository.delete(existingUser);
		
	}



	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmailId(email);
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
    
}

