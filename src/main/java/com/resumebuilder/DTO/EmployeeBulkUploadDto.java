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
    
    public void setRemark(Object remark) {
        if (remark instanceof List<?>) {
            this.remark = (List<String>) remark;
        } else if (remark instanceof String) {
            // If remark is a single string, convert it to a list with one element
            this.remark = Collections.singletonList((String) remark);
        }
    }
    
}
