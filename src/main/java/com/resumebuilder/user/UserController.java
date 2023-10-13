package com.resumebuilder.user;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.resumebuilder.security.approle.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.auth.SignupRequest;
//import com.resumebuilder.security.approle.AppRole;
//import com.resumebuilder.security.approle.AppRoleRepository;
import com.resumebuilder.security.approle.ERole;
import com.resumebuilder.security.jwt.JwtUtils;
import com.resumebuilder.security.response.MessageResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
//@RequestMapping("/users")

public class UserController {
    @Autowired
    private SecurityContextLogoutHandler logoutHandler;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;


    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/auth/id/{id}")
    public ResponseEntity<User> findUserByIdHandler(@PathVariable Long id) {

        User user = userService.findUserByIdUser(id);

        return new ResponseEntity<User>(user, HttpStatus.OK);
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

//	//add user api
//    @PostMapping("/add/employee")
//    //@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
//    public ResponseEntity<User> addUser(@RequestBody User user) {
//        User addedUser = userService.addUser(user);
//        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
//    }


    @PostMapping("/add/employee")
    public ResponseEntity<?> addUser(@Valid @RequestBody SignupRequest signUpRequest, Principal principal) {
    	User currentuser = userRepository.findByEmailId(principal.getName());
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
        user.setLinkedin_lnk(signUpRequest.getLinkedin_lnk());
        user.setPortfolio_link(signUpRequest.getPortfolio_link());
        user.setBlogs_link(signUpRequest.getBlogs_link());
        user.setModified_by(currentuser.getFull_name());

        String strRoles = signUpRequest.getRole();
        //Set<AppRole> roles = new HashSet<>();
        //System.out.println(roles);
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

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Employee data added successfully."));

    }

    //update user api
    @PutMapping("/edit/employee/{userId}")
    //@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<User> editUser(@PathVariable Long userId, @RequestBody User updatedUser, Principal principal) {
        User editedUser = userService.editUser(userId, updatedUser,principal);
        return new ResponseEntity<>(editedUser, HttpStatus.OK);
    }

    //delete user api
    @DeleteMapping("/delete/employee/{userId}")
    //@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}