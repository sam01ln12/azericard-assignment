package com.example.msuser.error;

public class UserNotFoundException extends CommonException{
    public UserNotFoundException(String errorMessage) {
        super(ErrorCodes.USER_NOT_FOUND, errorMessage);
    }
}
