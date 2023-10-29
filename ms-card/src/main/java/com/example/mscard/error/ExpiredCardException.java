package com.example.mscard.error;

public class ExpiredCardException extends CommonException{
    public ExpiredCardException(String errorMessage) {
        super(ErrorCodes.EXPIRED_CARD, errorMessage);
    }
}
