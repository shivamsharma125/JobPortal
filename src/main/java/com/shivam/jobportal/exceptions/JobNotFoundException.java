package com.shivam.jobportal.exceptions;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String message){
        super(message);
    }
}
