package com.example.mscard.error;

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
public class CardErrorHandler {

    @ExceptionHandler(value = CardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFoundException(CardNotFoundException ex) {

        log.error("Card not found, reference: {}, code: {}, message: {}",
                ex.getErrorUUID(), ex.getErrorCode(), ex.getErrorMessage());

        return new ErrorResponse(ErrorCodes.CARD_NOT_FOUND.name(), ex.getErrorMessage());
    }

    @ExceptionHandler(value = CardAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductAlreadyExistsException(CardAlreadyExistsException ex) {

        log.error("Card already exists, reference: {}, code: {}, message: {}",
                ex.getErrorUUID(), ex.getErrorCode(), ex.getErrorMessage());

        return new ErrorResponse(ErrorCodes.CARD_EXISTS.name(), ex.getErrorMessage());
    }

    @ExceptionHandler(value = CardInsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductHasEmptyStockException(CardInsufficientBalanceException ex) {

        log.error("Card has not enough balance, reference: {}, code: {}, message: {}",
                ex.getErrorUUID(), ex.getErrorCode(), ex.getErrorMessage());

        return new ErrorResponse(ErrorCodes.INSUFFICIENT_BALANCE.name(), ex.getErrorMessage());
    }

    @ExceptionHandler(value = ExpiredCardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleExpiredCardException(ExpiredCardException ex) {

        log.error("Card is expired, reference: {}, code: {}, message: {}",
                ex.getErrorUUID(), ex.getErrorCode(), ex.getErrorMessage());

        return new ErrorResponse(ErrorCodes.EXPIRED_CARD.name(), ex.getErrorMessage());
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
