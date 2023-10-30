package com.example.mscard.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotNull(message = "Username cannot be null")
    private String username;

    @Pattern(regexp = "^[0-9]{6}\\*{6}[0-9]{4}$", message = "Masked pan must be in format 123456******1234")
    @NotNull(message = "Masked pan cannot be null")
    private String maskedPan;
    @Pattern(regexp = "(?:0[1-9]|1[0-2])/[0-9]{2}", message = "Expiration must be in format MM/YY")
    @NotNull(message = "Expiration date cannot be null")
    private String expirationDate;

    @Pattern(regexp = "\\d{3}", message = "CVV length must be 3 and contain only digits")
    @NotNull(message = "CVV cannot be null")
    private String cvv;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Operation Type cannot be null")
    private OperationType operationType;
}
