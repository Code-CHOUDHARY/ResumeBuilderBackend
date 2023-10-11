package com.resumebuilder.exception;


/**
 * Custom exception to indicate that a technology is not found.
 */

public class TechnologyNotFoundException extends TechnologyException {
	
	public TechnologyNotFoundException(String message) {
        super(message);
    }
}
