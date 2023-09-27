package com.resumebuilder.exception;

public class CustomErrorResponse {
	
	 private String errorMessage;
	    private String errorDetails;

	    public CustomErrorResponse(String errorMessage, String errorDetails) {
	        this.errorMessage = errorMessage;
	        this.errorDetails = errorDetails;
	    }

	    public String getErrorMessage() {
	        return errorMessage;
	    }

	    public void setErrorMessage(String errorMessage) {
	        this.errorMessage = errorMessage;
	    }

	    public String getErrorDetails() {
	        return errorDetails;
	    }

	    public void setErrorDetails(String errorDetails) {
	        this.errorDetails = errorDetails;
	    }

}
