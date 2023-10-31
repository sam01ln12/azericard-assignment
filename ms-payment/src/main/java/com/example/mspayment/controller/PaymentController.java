package com.example.mspayment.controller;

import com.example.mspayment.model.PurchaseRequest;
import com.example.mspayment.model.TransactionDto;
import com.example.mspayment.model.TransactionRequest;
import com.example.mspayment.service.TransactionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {

    private final TransactionService transactionService;

    @PostMapping("/purchase")
    public TransactionDto buyProduct(@RequestHeader ("X-Payment-Data") @NotEmpty String paymentData,
             @Parameter(name = "X-Payment-Data", in = ParameterIn.HEADER,
                     schema = @Schema(type = "string")) String paymentDataHeader,
            @RequestBody @Valid PurchaseRequest purchaseRequest) {

        return transactionService.purchaseProduct(purchaseRequest, paymentData);
    }

    @PostMapping("/reverse/{transaction-id}")
    public TransactionDto reverseProduct(@PathVariable ("transaction-id") Long transactionId) {

        return transactionService.reverseProduct(transactionId);
    }

    @GetMapping("/transactions")
    public List<TransactionDto> getTransactions(@RequestBody @Valid TransactionRequest transactionRequest) {

        return transactionService.getAllTransactionByStates(transactionRequest);
    }
}
