package com.resumebuilder.exception;

import java.util.List;

public class DuplicateDataEntryException extends RuntimeException {
    public DuplicateDataEntryException(String errorMessage) {
        super(errorMessage);
    }


}