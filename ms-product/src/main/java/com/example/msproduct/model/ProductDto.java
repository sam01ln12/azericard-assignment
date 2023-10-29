package com.example.msproduct.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDto {

    @NotBlank(message = "Product name cannot be empty or blank")
    private String productName;

    @Min(value = 1, message = "Stock must be positive")
    @NotNull(message = "Stock cannot be null")
    private BigInteger stock;

    @DecimalMin(value = "0.01", message = "Price must be positive")
    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
}
