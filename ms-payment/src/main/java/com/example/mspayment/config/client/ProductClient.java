package com.example.mspayment.config.client;

import com.example.mspayment.error.CommonException;
import com.example.mspayment.model.ProductDto;
import feign.error.ErrorHandling;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name = "ms-product", url = "http://ms-product:8280/product")
public interface ProductClient {

    @ErrorHandling(defaultException = CommonException.class)
    @PutMapping("/buy/{product-name}")
    ProductDto buyProduct(@PathVariable("product-name") String productName);

    @ErrorHandling(defaultException = CommonException.class)
    @PutMapping("/reverse/{product-name}")
    ProductDto reverseProduct(@PathVariable("product-name") String productName);
}
