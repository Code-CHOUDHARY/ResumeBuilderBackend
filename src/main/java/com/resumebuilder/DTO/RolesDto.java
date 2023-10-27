package com.resumebuilder.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesDto {
	
	
	private String role_name;
	private List<String> remark;
    private boolean status;

}
