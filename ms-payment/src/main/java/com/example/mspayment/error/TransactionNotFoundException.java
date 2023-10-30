package com.example.mspayment.error;

public class TransactionNotFoundException extends CommonException{
    public TransactionNotFoundException(String errorMessage) {
        super(ErrorCodes.TRANSACTION_NOT_FOUND, errorMessage);
    }
}
