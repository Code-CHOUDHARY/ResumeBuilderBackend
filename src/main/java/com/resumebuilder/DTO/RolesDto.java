package com.resumebuilder.DTO;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesDto {
	
	
	private String role_name;
	private LocalDateTime modifiedOn;
	private String modifiedBy;
	private List<String> remark;
    private boolean status;
    private Long role_id;
	private String modified_by; // according to token store the name of current user in database
	@UpdateTimestamp
	private LocalDateTime modified_on;
}
