package com.example.mscard.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
@CrossOrigin
public class CardController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
