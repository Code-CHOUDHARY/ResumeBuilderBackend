package com.resumebuilder.user;

import java.util.List;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.UserNotFoundException;


@Service
public class UserServiceImplementation implements UserService{
	
	@Autowired

	private UserRepository userRepository;

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
				saveUser.setModified_by(user.getModified_by());
				
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
        return userRepository.save(existingUser);
	}

    //delete the user 
	@Override
	public void deleteUserById(Long userId) {
		User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));     
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

