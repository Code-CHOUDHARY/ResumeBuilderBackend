package com.resumebuilder.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.security.jwt.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
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
	
	
	

}
