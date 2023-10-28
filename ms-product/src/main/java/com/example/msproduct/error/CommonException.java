package com.example.msproduct.error;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CommonException extends RuntimeException{

    private final String errorUUID;
    private final ErrorCodes errorCode;
    private final String errorMessage;

    public CommonException(String errorUUID, ErrorCodes errorCode, String errorMessage) {
        super(errorMessage);
        this.errorUUID = errorUUID;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public CommonException(ErrorCodes errorCode, String errorMessage) {
        this(UUID.randomUUID().toString(), errorCode, errorMessage);
    }
}
