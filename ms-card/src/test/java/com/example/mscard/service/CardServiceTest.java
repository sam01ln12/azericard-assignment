package com.example.mscard.service;

import com.example.mscard.error.CardAlreadyExistsException;
import com.example.mscard.error.CardInsufficientBalanceException;
import com.example.mscard.error.CardNotFoundException;
import com.example.mscard.error.ExpiredCardException;
import com.example.mscard.model.*;
import com.example.mscard.repository.CardRepository;
import com.google.common.hash.Hashing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository mockCardRepository;

    private CardService cardService;

    private CardRequest cardRequest;
    private OperationRequest operationRequest;
    private Card card;

    private String cvv;


    @BeforeEach
    void setUp() {
        cardService = new CardService(mockCardRepository);

        cardRequest = new CardRequest("username", "1234567890123456", "11/24", "111",
                BigDecimal.TEN);

        operationRequest = OperationRequest.builder()
                .username("username")
                .maskedPan("123456******3456")
                .expirationDate("11/24")
                .cvv("111")
                .price(BigDecimal.ONE)
                .operationType(OperationType.PURCHASE)
                .build();

        card = Card.builder()
                .username("username")
                .pan("1234567890123456")
                .maskedPan("123456******3456")
                .expirationDate(LocalDate.of(2024, 12, 1))
                .cvv("111")
                .balance(BigDecimal.TEN)
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .build();

        cvv = Hashing.sha256().hashString("111", StandardCharsets.UTF_8).toString();
    }

    @Test
    void testAddNewCardThrowsCardAlreadyExistsException() {

        when(mockCardRepository.findByPanAndCvvAndExpirationDateAndUsername(anyString(), anyString(),
               any(LocalDate.class) , anyString())).thenReturn(Optional.ofNullable(card));

        assertThrows(CardAlreadyExistsException.class, () -> cardService.addNewCard(cardRequest));
    }

    @Test
    void testAddNewCard() {

        when(mockCardRepository.findByPanAndCvvAndExpirationDateAndUsername("1234567890123456", cvv,
                LocalDate.of(2024, 12, 1), "username")).thenReturn(Optional.empty());

        cardService.addNewCard(cardRequest);
        verify(mockCardRepository).saveAndFlush(any(Card.class));
    }

    @Test
    void testAddNewCardExpiredCard() {

        cardRequest.setExpirationDate("11/22");
        assertThrows(ExpiredCardException.class, () -> cardService.addNewCard(cardRequest));
    }

    @Test
    void testCreateOperationPurchase() {

        when(mockCardRepository.findByMaskedPanAndCvvAndExpirationDateAndUsername("123456******3456", cvv,
                LocalDate.of(2024, 12, 1), "username")).thenReturn(Optional.ofNullable(card));

        cardService.createOperation(operationRequest);
        verify(mockCardRepository).save(any(Card.class));
    }

    @Test
    void testCreateOperationPurchaseExpiredCard() {

        card.setExpirationDate(LocalDate.of(2022, 12, 1));
        operationRequest.setExpirationDate("11/22");
        when(mockCardRepository.findByMaskedPanAndCvvAndExpirationDateAndUsername("123456******3456", cvv,
                LocalDate.of(2022, 12, 1), "username")).thenReturn(Optional.ofNullable(card));

        assertThrows(ExpiredCardException.class, () -> cardService.createOperation(operationRequest));
    }

    @Test
    void testCreateOperationPurchaseInsufficientBalance() {

        operationRequest.setPrice(new BigDecimal(100));
        when(mockCardRepository.findByMaskedPanAndCvvAndExpirationDateAndUsername("123456******3456", cvv,
                LocalDate.of(2024, 12, 1), "username")).thenReturn(Optional.ofNullable(card));

        assertThrows(CardInsufficientBalanceException.class, () ->cardService.createOperation(operationRequest));
    }

    @Test
    void testCreateOperationReverse() {

        operationRequest.setOperationType(OperationType.REVERSE);

        when(mockCardRepository.findByMaskedPanAndCvvAndExpirationDateAndUsername("123456******3456", cvv,
                LocalDate.of(2024, 12, 1), "username")).thenReturn(Optional.ofNullable(card));

        cardService.createOperation(operationRequest);
        verify(mockCardRepository).save(any(Card.class));
    }

    @Test
    void testCreateOperationThrowsCardNotFoundException() {

        when(mockCardRepository.findByMaskedPanAndCvvAndExpirationDateAndUsername("123456******3456", cvv,
                LocalDate.of(2024, 12, 1), "username")).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.createOperation(operationRequest));

    }

    @Test
    void testCreateOperationThrowsOptimisticLockingFailureException() {

        when(mockCardRepository.findByMaskedPanAndCvvAndExpirationDateAndUsername("123456******3456", cvv,
                LocalDate.of(2024, 12, 1), "username")).thenReturn(Optional.ofNullable(card));

        when(mockCardRepository.save(any(Card.class))).thenThrow(OptimisticLockingFailureException.class);

        assertThrows(OptimisticLockingFailureException.class, () -> cardService.createOperation(operationRequest));
    }

    @Test
    void testGetActiveCards() {

        final Page<Card> cards = new PageImpl<>(List.of(card));
        when(mockCardRepository.findAllByExpirationDateAfterAndUsername(LocalDate.now(), "username",
                PageRequest.of(0, 10))).thenReturn(cards);

        final List<CardDto> result = cardService.getActiveCards("username", 0, 10);
        assertEquals(1, result.size());

    }

    @AfterEach
    void tearDown() {

        cvv = null;
        card = null;
        cardRequest = null;
        operationRequest = null;
    }
}
