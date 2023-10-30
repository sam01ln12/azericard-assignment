package com.example.mspayment.controller;

import com.example.mspayment.model.CardDto;
import com.example.mspayment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {

    private final TransactionService transactionService;

    @GetMapping("/test/{username}")
    public List<CardDto> test(@PathVariable String username) {

        return transactionService.getActiveCardsByUsername(username);
    }
}
