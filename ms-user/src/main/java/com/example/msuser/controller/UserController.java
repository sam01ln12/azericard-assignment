package com.example.msuser.controller;

import com.example.msuser.model.*;
import com.example.msuser.service.UserOperationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserOperationService userOperationService;

    @PostMapping("/register")
    public String registerNewUser(@RequestBody @Valid AuthRequest authRequest) {
        return userOperationService.register(authRequest);
    }

    @PostMapping("generate-token")
    public String authenticate(@RequestBody @Valid AuthRequest authRequest) {

        return userOperationService.generateToken(authRequest);
    }

    @PostMapping("/buy")
    public TransactionDto buyProduct(HttpServletRequest request,
                                     @RequestBody @Valid PaymentRequest paymentRequest) {

        return userOperationService.buyProduct(paymentRequest, request);
    }

    @GetMapping("/cards")
    public List<CardDto> getActiveCards(HttpServletRequest request,
                                        @RequestParam (defaultValue = "0") int pageNumber,
                                        @RequestParam (defaultValue = "20") int pageSize) {



        return userOperationService.getActiveCards(request, pageNumber, pageSize);
    }

    @GetMapping("/products")
    public List<ProductDto> getAvailableProducts(@RequestParam (defaultValue = "0") int pageNumber,
                                                 @RequestParam (defaultValue = "20") int pageSize) {

        return userOperationService.getAvailableProducts(pageNumber, pageSize);
    }
}
