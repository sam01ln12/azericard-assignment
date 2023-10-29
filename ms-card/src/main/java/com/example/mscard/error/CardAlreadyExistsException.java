package com.example.mscard.error;

public class CardAlreadyExistsException extends CommonException{
    public CardAlreadyExistsException(String errorMessage) {
        super(ErrorCodes.CARD_EXISTS, errorMessage);
    }
}
