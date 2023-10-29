package com.example.msproduct.controller;

import com.example.msproduct.model.ProductDto;
import com.example.msproduct.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<ProductDto> getAllAvailableProducts(@RequestParam (defaultValue = "0") int pageNumber,
                                                    @RequestParam (defaultValue = "20" )int pageSize) {

        return productService.getAvailableProducts(pageNumber, pageSize);
    }

    @PostMapping("/add")
    public ProductDto addProduct(@RequestBody @Valid ProductDto productDto) {

        return productService.addProduct(productDto);
    }

    @PutMapping("/buy/{product-name}")
    public ProductDto buyProduct(@PathVariable ("product-name") String productName) {

        return productService.buyProduct(productName);
    }

    @PutMapping("/reverse/{product-name}")
    public ProductDto reverseProduct(@PathVariable ("product-name") String productName) {

        return productService.reverseProduct(productName);
    }
}
