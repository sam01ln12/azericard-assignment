package com.example.msuser.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequest {

    @Pattern(regexp = "^[0-9]{6}\\*{6}[0-9]{4}$", message = "Masked pan must be in format 123456******1234")
    @NotNull(message = "Masked pan cannot be null")
    private String maskedPan;

    @Pattern(regexp = "(?:0[1-9]|1[0-2])/[0-9]{2}", message = "Expiration date must be in format MM/YY")
    @NotNull(message = "Expiration date cannot be null")
    private String expirationDate;

    @Pattern(regexp = "\\d{3}", message = "CVV length must be 3 and contain only digits")
    @NotNull(message = "CVV cannot be null")
    private String cvv;

    @NotEmpty(message = "Product name cannot be empty or blank")
    private String productName;
}
