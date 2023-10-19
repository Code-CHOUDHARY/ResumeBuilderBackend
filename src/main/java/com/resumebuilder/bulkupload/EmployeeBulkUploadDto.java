package com.resumebuilder.bulkupload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
