package com.intuit.craftdemo.exception;

import com.intuit.craftdemo.controller.AdminController;
import com.intuit.craftdemo.controller.DriverController;
import com.intuit.craftdemo.dto.ErrorMessage;
import com.intuit.craftdemo.util.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(assignableTypes = {DriverController.class, AdminController.class})
@ResponseStatus
public class ModuleExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(UserAlreadyLoggedInException.class)
    public ResponseEntity<?> userAlreadyLoggedInException(UserAlreadyLoggedInException exception, 
                                                          WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.builder()
                        .error_code(exception.getErrorCode())
                        .error_message(exception.getMessage()).build());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<?> userAlreadyExistException(UserAlreadyExistException exception,
                                                          WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.builder()
                        .error_code(exception.getErrorCode())
                        .error_message(exception.getMessage()).build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundException(UserNotFoundException exception,
                                                          WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder()
                        .error_code(exception.getErrorCode())
                        .error_message(exception.getMessage()).build());
    }

    @ExceptionHandler(InvalidUserPasswordException.class)
    public ResponseEntity<?> invalidUserPasswordException(InvalidUserPasswordException exception,
                                                          WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorMessage.builder()
                        .error_code(exception.getErrorCode())
                        .error_message(exception.getMessage()).build());
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<?> authenticationFailedException(AuthenticationFailedException exception,
                                                          WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorMessage.builder()
                        .error_code(exception.getErrorCode())
                        .error_message(exception.getMessage()).build());
    }

    @ExceptionHandler(DriverOnbaordingException.class)
    public ResponseEntity<?> authenticationFailedException(DriverOnbaordingException exception,
                                                           WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.builder()
                        .error_code(exception.getErrorCode())
                        .error_message(exception.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> genericException(Exception exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.builder()
                        .error_code(ResponseMessage.ErrorCodes.GENERIC_ERROR.toString())
                        .error_message(exception.getMessage()).build());
    }
}
