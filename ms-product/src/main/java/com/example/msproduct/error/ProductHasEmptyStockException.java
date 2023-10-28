package com.example.msproduct.error;

public class ProductHasEmptyStockException extends CommonException{
    public ProductHasEmptyStockException(String errorMessage) {
        super(ErrorCodes.PRODUCT_HAS_EMPTY_STOCK, errorMessage);
    }
}
