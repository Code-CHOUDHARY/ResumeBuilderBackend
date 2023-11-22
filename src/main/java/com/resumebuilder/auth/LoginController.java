package com.resumebuilder.auth;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.resumebuilder.security.approle.AppRole;
//import com.resumebuilder.security.approle.AppRoleRepository;
import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.approle.UserRole;
import com.resumebuilder.security.jwt.JwtUtils;
import com.resumebuilder.security.response.JwtResponse;
import com.resumebuilder.security.response.MessageResponse;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserDetailsImpl;
import com.resumebuilder.user.UserRepository;
import com.resumebuilder.user.UserRoleRepository;
import com.resumebuilder.user.UserServiceImplementation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {	  
		
	
	  private final AuthenticationManager authenticationManager;	  
	  private final UserRepository userRepository;	  
	  private final UserRoleRepository roleRepository;  
	 // private final UserRoleRepository userRoleRepository;
	  private final PasswordEncoder encoder;
	  private final JwtUtils jwtUtils;
		private final static Logger logger = LogManager.getLogger(UserServiceImplementation.class);

	  @PostMapping("/signin")
	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	      try {
	          // Authenticate user
	          Authentication authentication = authenticationManager.authenticate(
	                  new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	          // Check if the user is active (is_deleted = false)
	          UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

	          // Retrieve users from the repository based on email
	          List<User> users = userRepository.findByEmailIds(userDetails.getEmail());
	          logger.info("Users found for email {}");
	          // Filter active users
	          List<User> activeUsers = users.stream()
	                  .filter(user -> !user.is_deleted())
	                  .collect(Collectors.toList());

	          // Check if there are any active users
	          if (activeUsers.isEmpty()) {
	              // No active user found, return an unauthorized response
	              return ResponseEntity
	                      .status(HttpStatus.UNAUTHORIZED)
	                      .body(new MessageResponse("Error: No active user found."));
	          }

	          // If there are multiple active users, you might want to choose one (e.g., the first one) or handle it accordingly

	          // User is active, proceed with generating the JWT token
	          SecurityContextHolder.getContext().setAuthentication(authentication);
	          String jwt = jwtUtils.generateJwtToken(authentication);

	          List<String> roles = userDetails.getAuthorities().stream()
	                  .map(item -> item.getAuthority())
	                  .collect(Collectors.toList());

	          return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
	                  userDetails.getEmail(), roles));
	      } catch (BadCredentialsException ex) {
	          return ResponseEntity
	                  .status(HttpStatus.UNAUTHORIZED)
	                  .body(new MessageResponse("Error: Bad credentials provided."));
	      }
	  }

//
//	  @PostMapping("/signin")
//	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//	      try {
//	          Authentication authentication = authenticationManager.authenticate(
//	              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//	          SecurityContextHolder.getContext().setAuthentication(authentication);
//	          String jwt = jwtUtils.generateJwtToken(authentication);
//
//	          UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//	          List<String> roles = userDetails.getAuthorities().stream()
//	              .map(item -> item.getAuthority())
//	              .collect(Collectors.toList());
//
//	          return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
//	                  userDetails.getEmail(), roles));
//	      } catch (BadCredentialsException ex) {
//	          return ResponseEntity
//	              .status(HttpStatus.UNAUTHORIZED)
//	              .body(new MessageResponse("Error: Bad credentials provided."));
//	      }
//	  }


	  

	  
	  @PostMapping("/signup")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		 System.out.println(signUpRequest.getEmail()+signUpRequest.getRole());
		// Create a new User instance and map the fields from SignupRequest
		    User user = new User(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
		    user.setFull_name(signUpRequest.getFull_name());
		    user.setEmployee_Id(signUpRequest.getEmployee_Id());
		    user.setCurrent_role(signUpRequest.getCurrent_role());
		    user.setUser_image(signUpRequest.getUser_image());
		    user.setGender(signUpRequest.getGender());
		    user.setMobile_number(signUpRequest.getMobile_number());
		    user.setLocation(signUpRequest.getLocation());
		    user.setDate_of_joining(signUpRequest.getDate_of_joining());
		    user.setDate_of_birth(signUpRequest.getDate_of_birth()); 
		    
		    String strRoles = signUpRequest.getRole();
	        if (strRoles == null) {
	            UserRole userRole = roleRepository.findByName(ERole.ROLE_USER);
	            user.setAppRole(userRole);
	        } else {
	            switch (strRoles) {
	                case "admin":
	                    UserRole adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
	                    user.setAppRole(adminRole);

	                    break;
	                case "manager":
	                    UserRole managerRole = roleRepository.findByName(ERole.ROLE_MANAGER);
	                    user.setAppRole(managerRole);

	                    break;
	                default:
	                    UserRole userRole = roleRepository.findByName(ERole.ROLE_USER);
	                    user.setAppRole(userRole);

	            }
	        }

	        userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	  }

	  @PostMapping("/logout")
	  public ResponseEntity<?> logoutUser() {


	        jwtUtils.generateInvalidatedJwtToken();

	            return ResponseEntity.ok(new MessageResponse("Logout successful."));
	        }            

}
