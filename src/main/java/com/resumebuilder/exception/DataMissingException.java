package com.resumebuilder.exception;

public class DataMissingException extends RuntimeException {
	String resourceName;
	String fieldName;
	long fieldvalue;
	public DataMissingException(String resourceName, String fieldName, long fieldvalue) {
		super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldvalue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldvalue = fieldvalue;
	}
	
	
//	public DataMissingException(String errorMessage) {
//        super(errorMessage);
//    }
}
