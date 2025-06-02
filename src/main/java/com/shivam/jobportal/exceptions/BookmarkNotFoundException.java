package com.shivam.jobportal.exceptions;

public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException(String message) {
        super(message);
    }
}
