package com.shivam.jobportal.advices;

import com.shivam.jobportal.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handles resource duplication scenarios.
    // Returns HTTP 409 Conflict.
    @ExceptionHandler({UserAlreadyExistsException.class, BookmarkAlreadyExistException.class,
            JobApplicationAlreadyExistException.class})
    public ResponseEntity<String> handleConflict(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Specific handling for unexpected runtime errors.
    // Returns HTTP 500 Internal Server Error.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles client-side errors regarding invalid input data.
    // Returns HTTP 400 Bad Request.
    @ExceptionHandler({InvalidRoleException.class, InvalidRequestException.class})
    public ResponseEntity<String> handleBadRequest(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    // Handles authentication failures (invalid login/credentials).
    // Returns HTTP 401 Unauthorized.
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleUnAuthorizedState(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    // Handles cases where a requested resource is missing in the database.
    // Returns HTTP 404 Not Found.
    @ExceptionHandler({JobNotFoundException.class,UserNotFoundException.class,
            BookmarkNotFoundException.class,JobApplicationNotFoundException.class})
    public ResponseEntity<String> handleNotFound(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Handles access denial when user lacks necessary permissions.
    // Returns HTTP 403 Forbidden.
    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<String> handleForbidden(Exception ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
