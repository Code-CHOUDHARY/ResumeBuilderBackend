package com.resumebuilder.exception;

public class DataMissingException extends RuntimeException {
    public DataMissingException(String errorMessage) {
        super(errorMessage);
    }
}
