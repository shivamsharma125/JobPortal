package com.shivam.jobportal.exceptions;

public class BookmarkAlreadyExistException extends RuntimeException{
    public BookmarkAlreadyExistException(String message) {
        super(message);
    }
}
