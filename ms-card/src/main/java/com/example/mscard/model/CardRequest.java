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
public class CardRequest {

    @NotNull(message = "Username cannot be null")
    private String username;

    @Pattern(regexp = "\\d{16}", message = "Pan length must be 16 and contain only digits")
    @NotNull(message = "Pan cannot be null")
    private String pan;

    @Pattern(regexp = "(?:0[1-9]|1[0-2])/[0-9]{2}", message = "Expiration date must be in format MM/YY")
    @NotNull(message = "Expiration date cannot be null")
    private String expirationDate;

    @Pattern(regexp = "\\d{3}", message = "CVV length must be 3 and contain only digits")
    @NotNull(message = "CVV cannot be null")
    private String cvv;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.01", message = "Balance must be positive")
    private BigDecimal balance;
}
