package com.example.msuser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CardDto {

    private String username;
    private String maskedPan;
    private String expirationDate;
    private BigDecimal balance;
}
