package com.example.mspayment.config.client;

import com.example.mspayment.model.CardDto;
import com.example.mspayment.model.OperationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-card", url = "http://ms-card:8380/card")
public interface CardClient {

    @PutMapping("/operation")
    CardDto createOperation(@RequestBody OperationRequest operationRequest);
}
