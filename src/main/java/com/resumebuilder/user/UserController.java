package com.resumebuilder.user;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.resumebuilder.auth.SignupRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    /**
     * Get user details by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user details.
     */
    
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
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
    
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER', 'USER')")
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



//    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
//    @PostMapping("/add/employee")
//    public ResponseEntity<?> addUser(@Valid @RequestBody SignupRequest signUpRequest, Principal principal) {
//    	
//    	User currentuser = userRepository.findByEmailId(principal.getName());
//
//        // Create a new User instance
//        User user = new User(signUpRequest.getEmail(), signUpRequest.getEmail()); // Set the initial password to be the same as the email
//        user.setFull_name(signUpRequest.getFull_name());
//        user.setEmployee_Id(signUpRequest.getEmployee_Id());
//        user.setCurrent_role(signUpRequest.getCurrent_role());
//        user.setUser_image(signUpRequest.getUser_image());
//        user.setGender(signUpRequest.getGender());
//        user.setMobile_number(signUpRequest.getMobile_number());
//        user.setLocation(signUpRequest.getLocation());
//        user.setDate_of_joining(signUpRequest.getDate_of_joining());
//        user.setDate_of_birth(signUpRequest.getDate_of_birth());
//        user.setLinkedin_lnk(signUpRequest.getLinkedin_lnk());
//        user.setPortfolio_link(signUpRequest.getPortfolio_link());
//        user.setBlogs_link(signUpRequest.getBlogs_link());
//        user.setModified_by(currentuser.getFull_name());
//
//        String strRoles = signUpRequest.getRole();
//
//        if (strRoles == null) {
//            UserRole userRole = roleRepository.findByName(ERole.ROLE_USER);
//            user.setAppRole(userRole);
//        } else {
//            switch (strRoles) {
//                case "admin":
//                    UserRole adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
//                    user.setAppRole(adminRole);
//
//                    break;
//                case "manager":
//                    UserRole managerRole = roleRepository.findByName(ERole.ROLE_MANAGER);
//                    user.setAppRole(managerRole);
//
//                    break;
//                default:
//                    UserRole userRole = roleRepository.findByName(ERole.ROLE_USER);
//                    user.setAppRole(userRole);
//
//            }
//        }
//
//        userRepository.save(user);
//
//        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee data added successfully."));
//    }
    
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

    //update user api
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

    //delete user api
    @DeleteMapping("/delete/employee/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, Principal principal) {
        userService.deleteUserById(userId, principal);
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    
}