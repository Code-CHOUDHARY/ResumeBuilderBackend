package com.resumebuilder.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ProjectNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException ex) {
        // Customize the response using your CustomErrorResponse class
        CustomErrorResponse errorResponse = new CustomErrorResponse("Project not found", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(ProjectDataNullException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleProjectDataNullException(ProjectDataNullException ex) {
		CustomErrorResponse errorResponse = new CustomErrorResponse("Project data is incomplete", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(CertificationsNullException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleCertificationsNullException(CertificationsNullException ex) {
		CustomErrorResponse errorResponse = new CustomErrorResponse("Certification data is incomplete", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(RoleException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleRoleException(RoleException ex) {
        return new CustomErrorResponse("Role exception", ex.getMessage());
    }

}
