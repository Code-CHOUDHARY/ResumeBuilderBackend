package com.resumebuilder.user;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryRepository;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.teamactivity.TeamActivityService;


@Service
public class UserServiceImplementation implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ActivityHistoryService activityHistoryService;
	
	@Autowired
	private ActivityHistoryRepository activityHistoryRepository;
	
	
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
	public User addUser(User user)throws UserNotFoundException {
		 try {
				User saveUser = new User();
				
				
//				User username = userRepository.findByEmailId(principal.getName());
				
				saveUser.setFull_name(user.getFull_name());
				saveUser.setEmail(user.getEmail());
				saveUser.setPassword(user.getPassword());
				saveUser.setEmployee_Id(user.getEmployee_Id());
				saveUser.setCurrent_role(user.getCurrent_role());
				saveUser.setAppRoles(user.getAppRoles());
				saveUser.setUser_image(user.getUser_image());
				saveUser.setGender(user.getGender());
				saveUser.setMobile_number(user.getMobile_number());
				saveUser.setLocation(user.getLocation());
				saveUser.setDate_of_joining(user.getDate_of_joining());
				saveUser.setDate_of_birth(user.getDate_of_birth());
				saveUser.setLinkedin_lnk(user.getLinkedin_lnk());
				saveUser.setPortfolio_link(user.getPortfolio_link());
				saveUser.setBlogs_link(user.getBlogs_link());
				saveUser.setModified_by(user.getFull_name());
				
				 UserToJsonConverter userToJsonConverter = new UserToJsonConverter();
				
				 String activityType = "Add Employee";
			     String description = "New Employee Added";
			     String newData = userToJsonConverter.convertUserToJSON(saveUser);
			     activityHistoryService.addActivity(activityType, description, newData, null, null);
				
				 return userRepository.save(saveUser);
				
			} catch (Exception e) {
				throw new UserNotFoundException("Failed to add user data."+e.getMessage());
			}     
	}
    
	
 // Update the existing user
    @Override
	public User editUser(Long userId, User updatedUser) {
    	User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        existingUser.setFull_name(updatedUser.getFull_name());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDate_of_birth(updatedUser.getDate_of_birth());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setLocation(updatedUser.getLocation());
        existingUser.setCurrent_role(updatedUser.getCurrent_role());
        existingUser.setEmployee_Id(updatedUser.getEmployee_Id());
        existingUser.setAppRoles(updatedUser.getAppRoles());
        existingUser.setUser_image(updatedUser.getUser_image());
        existingUser.setMobile_number(updatedUser.getMobile_number());
        existingUser.setDate_of_joining(updatedUser.getDate_of_joining());
        existingUser.setLinkedin_lnk(updatedUser.getLinkedin_lnk());
        existingUser.setPortfolio_link(updatedUser.getPortfolio_link());
        existingUser.setBlogs_link(updatedUser.getBlogs_link());    
        
     // Compare the fields and identify changes
        Map<String, String> changes = new HashMap<>();
        if (!Objects.equals(existingUser.getFull_name(), updatedUser.getFull_name())) {
            changes.put("full_name", updatedUser.getFull_name());
        }
        if (!Objects.equals(existingUser.getEmail(), updatedUser.getEmail())) {
            changes.put("email", updatedUser.getEmail());
        }
        if (!Objects.equals(existingUser.getDate_of_birth(), updatedUser.getDate_of_birth())) {
            changes.put("Date of Birth", updatedUser.getDate_of_birth());
        }
        if (!Objects.equals(existingUser.getGender(), updatedUser.getGender())) {
            changes.put("Gender", updatedUser.getGender());
        }
        if (!Objects.equals(existingUser.getLocation(), updatedUser.getLocation())) {
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
			    (activityType, description,newData ,oldData, null);
			}
		catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
      
        return userRepository.save(existingUser);
        }
	

    //delete the user 
	@Override
	public void deleteUserById(Long userId) {
		User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));   
		 String activityType = "Delete Employee";
	     String description = "Change in Employee Data";
	     
	    activityHistoryService.addActivity(activityType, description,"user with"+userId + "deleted", null, null);
		
        userRepository.delete(existingUser);
		
	}

//	//list of all users
//	@Override
//	public List<User> getAllUsers() {
//		return userRepository.findAll();
//	}


	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmailId(email);
	}
    
}

