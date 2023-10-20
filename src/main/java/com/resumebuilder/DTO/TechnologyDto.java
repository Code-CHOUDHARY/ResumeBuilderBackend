package com.resumebuilder.DTO;

import java.util.List;

import lombok.Data;

@Data
public class TechnologyDto {
		
	private String technology_name;
	private List<String> remark;
    private boolean status;

}
