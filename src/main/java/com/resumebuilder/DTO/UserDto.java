package com.resumebuilder.DTO;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
public class UserDto {
    private String employee_Id;
    private String full_name;
    private String date_of_joining;
    private String date_of_birth;
    private String current_role;
    private String email;
    private String gender;
    private String mobile_number;
    private String location;
    private String manager_employee_id;
    @UpdateTimestamp
	private LocalDateTime modified_on;

    
}
