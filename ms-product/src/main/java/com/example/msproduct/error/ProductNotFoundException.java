package com.example.msproduct.error;

public class ProductNotFoundException extends CommonException{
    public ProductNotFoundException(String errorMessage) {
        super(ErrorCodes.PRODUCT_NOT_FOUND, errorMessage);
    }
}
