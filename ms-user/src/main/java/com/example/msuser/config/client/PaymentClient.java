package com.example.msuser.config.client;

import com.example.msuser.error.CommonException;
import com.example.msuser.model.PurchaseRequest;
import com.example.msuser.model.TransactionDto;
import com.example.msuser.model.TransactionRequest;
import feign.error.ErrorHandling;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-payment", url = "http://ms-payment:8480/payment")
public interface PaymentClient {

    @ErrorHandling(defaultException = CommonException.class)
    @PostMapping("/purchase")
    TransactionDto buyProduct(@RequestHeader("X-Payment-Data") @NotEmpty String paymentData,
                                     @RequestBody @Valid PurchaseRequest purchaseRequest);

    @PostMapping("/reverse/{transaction-id}")
    TransactionDto reverseProduct(@PathVariable ("transaction-id") Long transactionId);

    @GetMapping("/transactions")
    List<TransactionDto> getTransactions(@RequestBody @Valid TransactionRequest transactionRequest);
}
