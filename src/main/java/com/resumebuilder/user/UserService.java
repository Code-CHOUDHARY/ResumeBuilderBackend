package com.resumebuilder.user;

import java.util.List;

public interface UserService {
	

	public List<User> getAllUsers();
	
	public User findUserByIdUser(Long id);

	public User findUserByUsername(String userName);


	public User editUser(Long userId, User updatedUser);
	public User addUser(User user);
	public void deleteUserById(Long userId);

	public User getUserByEmail(String email);

}

