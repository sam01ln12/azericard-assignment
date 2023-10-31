package com.example.msuser.error;

public class UserExistsException extends CommonException{
    public UserExistsException(String errorMessage) {
        super(ErrorCodes.USER_ALREADY_EXISTS, errorMessage);
    }
}
