package com.example.msuser.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class UserErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserExistsException.class)
    public ErrorResponse handleUserExistsException(UserExistsException ex) {
        log.error("User already exists, reference: {}, code: {}, message: {}",
                UUID.randomUUID(), ex.getErrorCode(), ex.getErrorMessage());

        return new ErrorResponse(ErrorCodes.USER_ALREADY_EXISTS.name(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User already exists, reference: {}, code: {}, message: {}",
                UUID.randomUUID(), ex.getErrorCode(), ex.getErrorMessage());

        return new ErrorResponse(ErrorCodes.USER_NOT_FOUND.name(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CommonException.class)
    public ErrorResponse handleCommonException(CommonException ex) {
        log.error("Error unexpected internal server error, reference: {}, code: {}, message: {}",
                UUID.randomUUID(), ex.getErrorCode(), ex.getErrorMessage());

        return new ErrorResponse(ErrorCodes.UNEXPECTED_ERROR.name(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalServerErrors(Exception ex) {
        log.error("Error unexpected internal server error, reference: {}, message: {}",
                UUID.randomUUID(), ex.getMessage());

        return new ErrorResponse(ErrorCodes.UNEXPECTED_ERROR.name(), ex.getMessage());
    }
}
