package com.resumebuilder.user;

import java.security.Principal;
import java.util.List;

public interface UserService {
	

	public List<User> getAllUsers();
	
	public User findUserByIdUser(Long id);

	public User findUserByUsername(String userName);


	public User editUser(Long userId, User updatedUser, Principal principal);
	public User addUser(User user, Principal principal);
	public void deleteUserById(Long userId);

	public User getUserByEmail(String email);

}

