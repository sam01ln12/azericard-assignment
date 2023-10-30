package com.example.mspayment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OperationRequest {

    private String username;
    private String maskedPan;
    private String expirationDate;
    private String cvv;
    private BigDecimal price;
    private OperationType operationType;
}
