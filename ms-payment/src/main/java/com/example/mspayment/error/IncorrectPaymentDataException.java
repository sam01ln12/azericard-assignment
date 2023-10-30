package com.example.mspayment.error;

public class IncorrectPaymentDataException extends CommonException{
    public IncorrectPaymentDataException(String errorMessage) {
        super(ErrorCodes.INVALID_PAYMENT_DATA, errorMessage);
    }
}
