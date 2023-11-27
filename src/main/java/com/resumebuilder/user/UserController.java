package com.resumebuilder.user;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.DTO.UserDto;
import com.resumebuilder.auth.SignupRequest;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.security.response.MessageResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
	@Lazy
    @Autowired
    private UserService userService;
	@Lazy
    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Get a list of all users.
     *
     * @return A list of users.
     */
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<UserDto>> getAllUsersList() {
        List<UserDto> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    /**
     * Get user details by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user details.
     */
    
   
    @GetMapping("/auth/id/{id}")
    public ResponseEntity<User> findUserByIdHandler(@PathVariable Long id) {

        User user = userService.findUserByIdUser(id);
       
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    /**
     * Get user details of the currently logged-in user.
     *
     * @return The user details of the currently logged-in user.
     */
    
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @GetMapping("/auth/user")
    public ResponseEntity<User> getUserById() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(userName);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    /**
     * Add a new employee user.
     *
     * @param signUpRequest The user details for registration.
     * @param principal     Represents the user identity.
     * @return The response entity indicating the success or failure of the registration.
     */
    
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@PostMapping("/add/employee")
    public ResponseEntity<?> addUser(@Valid @RequestBody SignupRequest signUpRequest, Principal principal) {
    	return userService.addUser(signUpRequest, principal);
    }
    
    /**
     * Update user details by user ID.
     *
     * @param userId       The ID of the user to be updated.
     * @param updatedUser  The updated user details.
     * @param principal    Represents the user identity.
     * @return The response entity indicating the success or failure of the update.
     */
    
  //edit employee data
    @PutMapping("/editEmployee/{userId}")
    public ResponseEntity<?> editUser(@PathVariable Long userId, @RequestBody UserDto editUserRequest,
                                      Principal principal) {
        ResponseEntity<?> response;
        try {	
            response = userService.editEmployee(userId, editUserRequest, principal);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new MessageResponse("employee details not edit."));
        }
        return response;
    }

    //update user personal details
    @PutMapping("/edit/employee/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<User> editUser(@PathVariable Long userId, @RequestBody User updatedUser, Principal principal) {
        User editedUser = userService.editUser(userId, updatedUser,principal);
        return new ResponseEntity<>(editedUser, HttpStatus.OK);
    }
    
    /**
     * Delete a user by user ID.
     *
     * @param userId     The ID of the user to be deleted.
     * @param principal  Represents the user identity.
     * @return The response entity indicating the success or failure of the delete operation.
     */

//    //delete user api
    @DeleteMapping("/delete/employee/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId, Principal principal) {
        try {
            userService.deleteUserById(userId, principal);
            return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Failed to soft delete user: " + e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    
    //user all details using user id
    @GetMapping("/auth/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable Long userId) {
        return userService.getUserDetailsById(userId);
    }
    
}