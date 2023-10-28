package com.example.msproduct.error;

public class ProductAlreadyExistsException extends CommonException{
    public ProductAlreadyExistsException(String errorMessage) {
        super(ErrorCodes.PRODUCT_EXISTS, errorMessage);
    }
}
