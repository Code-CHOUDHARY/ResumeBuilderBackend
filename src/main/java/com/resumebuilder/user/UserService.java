package com.resumebuilder.user;

import java.util.List;

public interface UserService {
	
	public List<User> getAllUsers();
	
	public User findUserByIdUser(Long id);

	public User findUserByUsername(String userName);
}
