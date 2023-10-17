package com.resumebuilder.exception;

import java.util.List;

public class MultipleDataEntryException extends Exception {
    private List<String> errorMessages;

    public MultipleDataEntryException(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
