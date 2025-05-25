package com.shivam.jobportal.advices;

import com.shivam.jobportal.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleConflict(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidRoleException.class, InvalidRequestException.class})
    public ResponseEntity<String> handleBadRequest(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleUnAuthorizedState(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<String> handleNotFound(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<String> handleForbidden(Exception ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
