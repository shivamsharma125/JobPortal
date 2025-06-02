package com.shivam.jobportal.exceptions;

public class JobApplicationAlreadyExistException extends RuntimeException {
    public JobApplicationAlreadyExistException(String message) {
        super(message);
    }
}
