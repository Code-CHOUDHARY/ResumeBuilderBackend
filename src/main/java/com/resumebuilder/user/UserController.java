package com.resumebuilder.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
    private UserService userService;

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
    
    //all users list
    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


	

}