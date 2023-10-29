package com.example.mscard.error;

public class CardNotFoundException extends CommonException{
    public CardNotFoundException(String errorMessage) {
        super(ErrorCodes.CARD_NOT_FOUND, errorMessage);
    }
}
