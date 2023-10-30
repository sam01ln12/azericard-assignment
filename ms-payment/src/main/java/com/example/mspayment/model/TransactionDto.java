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
public class TransactionDto {

    private Long transactionId;
    private String username;
    private String maskedPan;
    private String expirationDate;
    private String productName;
    private BigDecimal price;
    private TransactionState state;
    private String declineReason;
}
