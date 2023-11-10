package com.resumebuilder.resumes;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {
	    private HttpStatus status;
	    private String type;
	    private String message;
	    private Object data;
}
