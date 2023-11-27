package com.resumebuilder.DTO;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordResponse {
	    private HttpStatus statusType;
	    private String messageType;
	    private String message;
	    private boolean status;
	    private String data;
	}


