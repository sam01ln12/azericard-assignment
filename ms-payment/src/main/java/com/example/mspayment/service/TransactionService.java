package com.example.mspayment.service;

import com.example.mspayment.config.client.CardClient;
import com.example.mspayment.model.CardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final CardClient cardClient;

    public List<CardDto> getActiveCardsByUsername(String username) {

        return cardClient.getActiveCards(username, 0, 20);
    }
}
