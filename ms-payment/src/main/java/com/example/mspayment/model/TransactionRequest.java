package com.example.mspayment.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequest {

    @NotBlank(message = "Username cannot be blank or empty")
    private String username;
    @NotEmpty(message = "At least one state must be specified")
    private List<TransactionState> states;
    @NotNull(message = "Page number cannot be null")
    @Min(value = 0)
    private int pageNumber = 0;
    @NotNull(message = "Page size cannot be null")
    @Min(value = 1)
    private int pageSize = 20;
}
