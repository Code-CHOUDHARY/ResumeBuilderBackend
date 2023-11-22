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
	
	@ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
        return new CustomErrorResponse("User Exception. ", ex.getMessage());
    }
	
	@ExceptionHandler(DataMissingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleDataMissingException(DataMissingException ex) {
        return new CustomErrorResponse("Data missing.", ex.getMessage());
    }
	
	@ExceptionHandler(DuplicateDataEntryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleDuplicateDataEntryException(DuplicateDataEntryException ex) {
        return new CustomErrorResponse("Duplicate data entry.", ex.getMessage());
    }
	
	@ExceptionHandler(TechnologyNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleTechnologyNotFoundException(TechnologyNotFoundException ex) {
        return new CustomErrorResponse("Technology not found.", ex.getMessage());
    }

    @ExceptionHandler(TechnologyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleTechnologyException(TechnologyException ex) {
        return new CustomErrorResponse("Technology exception.", ex.getMessage());
    }
    
    @ExceptionHandler(DataProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleDataProcessingException(DataProcessingException ex) {
        return new CustomErrorResponse(ex.getMessage());
    }
    
    @ExceptionHandler(MultipleDataEntryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomErrorResponse handleMultipleDataEntryException(MultipleDataEntryException ex) {
        return new CustomErrorResponse(ex.getMessage());
    }
    
    @ExceptionHandler(ProjectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomErrorResponse handleProjectException(ProjectException ex) {
        return new CustomErrorResponse(ex.getMessage());
    }
    
    @ExceptionHandler(EducationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomErrorResponse handleEducationException(EducationException ex) {
        return new CustomErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(CertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomErrorResponse handleCertificateNotFoundException(CertificateNotFoundException ex) {
        return new CustomErrorResponse(ex.getMessage());
    }
    
    @ExceptionHandler(ForgotPasswordException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleForgotPasswordException(ForgotPasswordException ex) {
        return new CustomErrorResponse("User not found with email. ", ex.getMessage());
    }
    
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorResponse handleTokenExpiredException(TokenExpiredException ex) {
        return new CustomErrorResponse("Your password reset link appears to be invalid. Please request a new link. ", ex.getMessage());
    }

}
