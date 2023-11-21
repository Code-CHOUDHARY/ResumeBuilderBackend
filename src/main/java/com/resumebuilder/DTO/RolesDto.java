package com.resumebuilder.DTO;

import java.time.LocalDateTime;
import java.util.List;

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

}
