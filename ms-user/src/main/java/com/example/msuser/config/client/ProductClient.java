package com.example.msuser.config.client;

import com.example.msuser.model.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "ms-product", url = "http://ms-product:8280/product")
public interface ProductClient {

    @GetMapping("/products")
    public List<ProductDto> getAllAvailableProducts(@RequestParam(defaultValue = "0") int pageNumber,
                                                    @RequestParam (defaultValue = "20" )int pageSize);
}
