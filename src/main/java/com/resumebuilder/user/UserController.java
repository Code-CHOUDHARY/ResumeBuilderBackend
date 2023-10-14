package com.resumebuilder.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.security.jwt.JwtUtils;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
//@RequestMapping("/api/users")

public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@GetMapping("/list")
//	@PreAuthorize("hasRole('ROLE_MANAGER')")
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
//	@PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
	
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	@GetMapping("/auth/id/{id}")
	public ResponseEntity<User> findUserByIdHandler(@PathVariable Long id){
		
		User user=userService.findUserByIdUser(id);
		
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
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

	//add user api
    @PostMapping("/add")
    //@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User addedUser = userService.addUser(user);
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }

    //update user api
    @PutMapping("/edit/{userId}")
    //@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<User> editUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User editedUser = userService.editUser(userId, updatedUser);
        return new ResponseEntity<>(editedUser, HttpStatus.OK);
    }
    
    //delete user api
    @DeleteMapping("/delete/{userId}")
    //@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
	

}