package com.resumebuilder.user;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.resumebuilder.auth.SignupRequest;

import jakarta.validation.Valid;

public interface UserService {
	

	public List<User> getAllUsers();
	
	public User findUserByIdUser(Long id);

	public User findUserByUsername(String userName);


	public User editUser(Long userId, User updatedUser, Principal principal);

	public ResponseEntity<?> addUser(SignupRequest signUpRequest, Principal principal);

	//public User addUser(User user, Principal principal);

	public void deleteUserById(Long userId, Principal principal);

	public User getUserByEmail(String email);
	//public List<User> getManagers();
	

}

