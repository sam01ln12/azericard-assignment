package com.example.mscard.controller;

import com.example.mscard.model.CardDto;
import com.example.mscard.model.CardRequest;
import com.example.mscard.model.OperationRequest;
import com.example.mscard.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card")
@CrossOrigin
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/cards/{username}")
    public List<CardDto> getActiveCards(@PathVariable String username,
                                        @RequestParam (defaultValue = "0") int pageNumber,
                                        @RequestParam (defaultValue = "20") int pageSize) {
        return cardService.getActiveCards(username, pageNumber, pageSize);
    }

    @PostMapping("/add")
    public CardDto addNewCard(@RequestBody @Valid CardRequest cardRequest) {

        return cardService.addNewCard(cardRequest);
    }

    @PutMapping("/operation")
    public CardDto createOperation(@RequestBody @Valid OperationRequest operationRequest) {


        return cardService.createOperation(operationRequest);
    }
}
