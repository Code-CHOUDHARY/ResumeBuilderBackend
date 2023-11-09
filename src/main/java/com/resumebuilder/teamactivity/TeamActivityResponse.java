package com.resumebuilder.teamactivity;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamActivityResponse {
		    private HttpStatus status;
		    private boolean type;
		    private String message;
		    private Object data;
	
}
