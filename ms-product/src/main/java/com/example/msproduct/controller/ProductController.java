package com.example.msproduct.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {

    @GetMapping("/test")
    public String test() {

        return "test";
    }
}
