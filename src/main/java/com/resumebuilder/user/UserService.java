package com.resumebuilder.user;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.resumebuilder.DTO.UserDto;
import com.resumebuilder.auth.SignupRequest;
import com.resumebuilder.exception.UserNotFoundException;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;

public interface UserService {
	

	public List<User> getAllUsers();
	
	public User findUserByIdUser(Long id) throws UserNotFoundException;

	public UserDto findUserByUsername(String userName) throws IOException, FileNotFoundException, java.io.IOException;


	public User editUser(Long userId, User updatedUser, Principal principal);

	public ResponseEntity<?> addUser(SignupRequest signUpRequest, Principal principal);

	//public User addUser(User user, Principal principal);

	public void deleteUserById(Long userId, Principal principal);

	public User getUserByEmail(String email);

	public boolean checkUserExists(String UserId);

	ResponseEntity<?> editEmployee(Long userId, UserDto editUserRequest, Principal principal);
	

}

