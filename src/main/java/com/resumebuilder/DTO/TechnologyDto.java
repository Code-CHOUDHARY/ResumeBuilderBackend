package com.resumebuilder.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class TechnologyDto {
		
	private Long technology_id;
	private String technology_name;
	private LocalDateTime modifiedOn;
	private String modifiedBy;
	private List<String> remark;
    private boolean status;

}
