package com.example.mspayment.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PurchaseRequest {

    @Pattern(regexp = "^[0-9]{6}\\*{6}[0-9]{4}$", message = "Masked pan must be in format 123456******1234")
    @NotNull(message = "Masked pan cannot be null")
    private String maskedPan;
    @NotNull(message = "Product name cannot be null")
    private String productName;
}
