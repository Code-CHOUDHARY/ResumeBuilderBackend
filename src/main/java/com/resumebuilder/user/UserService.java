package com.resumebuilder.user;

import java.util.List;

public interface UserService {
	
	public User editUser(Long userId, User updatedUser);
	public User addUser(User user);
	public void deleteUserById(Long userId);
	public List<User> getAllUsers();
	public User getUserByEmail(String email);

}