package com.example.mscard.error;

public class CardInsufficientBalanceException extends CommonException{
    public CardInsufficientBalanceException(String errorMessage) {
        super(ErrorCodes.INSUFFICIENT_BALANCE, errorMessage);
    }
}
