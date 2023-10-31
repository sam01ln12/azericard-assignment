package com.example.msuser.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequest {

    private String username;
    private List<TransactionState> states;
    private int pageNumber = 0;
    private int pageSize = 20;
}
