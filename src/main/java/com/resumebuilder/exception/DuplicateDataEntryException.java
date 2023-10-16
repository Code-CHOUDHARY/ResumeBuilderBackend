package com.resumebuilder.exception;

public class DuplicateDataEntryException extends RuntimeException {
    public DuplicateDataEntryException(String errorMessage) {
        super(errorMessage);
    }
}