package com.resumebuilder.auth;



import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class SignupRequest {
	
//	  @NotBlank
//	  @Size(max = 50)
//	  @Email
//	  private String email;
//	  
//	  private Set<String> role;
//	  
//	  @NotBlank
//	  @Size(min = 6, max = 40)
//	  private String password;
	
	
	private String full_name;
	private String email;
	private String role;
	private String password;	
	private String employee_Id;
	private String current_role;
	private String user_image;	
	private String gender;
	private String mobile_number;
	private String location;
	private String date_of_joining;
	private String date_of_birth;
	private String linkedin_lnk;
	private String portfolio_link;
	private String blogs_link;
	@UpdateTimestamp
	private LocalDateTime modified_on;
	private String modified_by;
	private boolean is_deleted;
//	@Column(name = "application_role_id")
//    private int applicationRoleId;
	  
	  

}
