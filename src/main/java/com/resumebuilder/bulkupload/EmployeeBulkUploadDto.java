package com.resumebuilder.bulkupload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBulkUploadDto {

    private String employeeId;
    private String fullName;
    private String dateOfJoining;
    private String dateOfBirth;
    private String currentRole;
    private String email;
    private String gender;
    private String mobile_number;
    private String location;
    private List<String> remark;
    private boolean status;
    
}
