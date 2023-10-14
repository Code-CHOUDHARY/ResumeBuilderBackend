package com.resumebuilder.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.security.approle.AppRole;
import com.resumebuilder.security.approle.AppRoleRepository;
import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.jwt.JwtUtils;
import com.resumebuilder.security.response.JwtResponse;
import com.resumebuilder.security.response.MessageResponse;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserDetailsImpl;
import com.resumebuilder.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {	  
		
	
	  private final AuthenticationManager authenticationManager;	  
	  private final UserRepository userRepository;	  
	  private final AppRoleRepository roleRepository;  
	  private final PasswordEncoder encoder;
	  private final JwtUtils jwtUtils;

	  @PostMapping("/signin")
	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	      try {
	          Authentication authentication = authenticationManager.authenticate(
	              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	          SecurityContextHolder.getContext().setAuthentication(authentication);
	          String jwt = jwtUtils.generateJwtToken(authentication);

	          UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
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

	  
	  @PostMapping("/signup")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		 System.out.println(signUpRequest.getEmail()+signUpRequest.getRole());
//	    if (UserRepository.existsByEmail(signUpRequest.getUsername())) {
//	      return ResponseEntity
//	          .badRequest()
//	          .body(new MessageResponse("Error: Username is already taken!"));
//	    }

	    // Create new user's account
	    User user = new User( signUpRequest.getEmail(),
	               encoder.encode(signUpRequest.getPassword()));

	    Set<String> strRoles = signUpRequest.getRole();
	    Set<AppRole> roles = new HashSet<>();
System.out.println(roles);
	    if (strRoles == null) {
	    	AppRole userRole = roleRepository.findByName(ERole.ROLE_USER)
	          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	      roles.add(userRole);
	    } else {
	      strRoles.forEach(role -> {
	        switch (role) {
	        case "admin":
	        	AppRole adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(adminRole);

	          break;
	        case "manager":
	        	AppRole managerRole = roleRepository.findByName(ERole.ROLE_MANAGER)
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(managerRole);

	          break;
	        default:
	        	AppRole userRole = roleRepository.findByName(ERole.ROLE_USER)
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(userRole);
	        }
	      });
	    }

	    user.setAppRoles(roles);
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	  }
	  
//	  @PostMapping("/logout")
//	  public ResponseEntity<?> logoutUser(HttpServletRequest request) {
//	      String token = jwtUtils.resolveToken(request);
//	      
//	      // Check if the token is valid and not expired
//	      if (jwtUtils.validateJwtToken(token)) {
//	          // Invalidate the token by creating a new one with a very short expiration
//	          String invalidatedToken = jwtUtils.generateInvalidatedJwtToken();
//	          
//	          return ResponseEntity.ok(new MessageResponse("Logout successful."));
//	      } else {
//	          return ResponseEntity.badRequest().body(new MessageResponse("Invalid token or token already expired."));
//	      }
//	  }

	  @PostMapping("/logout")
	  public ResponseEntity<?> logoutUser() {


	        jwtUtils.generateInvalidatedJwtToken();

	            return ResponseEntity.ok(new MessageResponse("Logout successful."));
	        } 
	           
	    

	    // Extract the token from the Authorization header
//	    private String extractTokenFromHeader(String authorizationHeader) {
//	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//	            return authorizationHeader.substring(7); // Remove "Bearer " prefix
//	        }
//	        return null;
//	    }

}
