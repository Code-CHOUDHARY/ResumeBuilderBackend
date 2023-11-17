package com.resumebuilder.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBulkUploadDto {

    private String employee_id;
    private String fullName;
    private String dateOfJoining;
    private String dateOfBirth;
    private String currentRole;
    private String email_id;
    private String gender;
    private String mobile_number;
    private String location;
    private List<String> remark;
    private boolean status;
    private String appRoleId;
    
}
