package com.example.mscard.service;

import com.example.mscard.error.CardAlreadyExistsException;
import com.example.mscard.error.CardInsufficientBalanceException;
import com.example.mscard.error.CardNotFoundException;
import com.example.mscard.error.ExpiredCardException;
import com.example.mscard.mapper.CardMapper;
import com.example.mscard.model.Card;
import com.example.mscard.model.CardDto;
import com.example.mscard.model.CardRequest;
import com.example.mscard.model.OperationRequest;
import com.example.mscard.repository.CardRepository;
import com.google.common.hash.Hashing;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.mscard.model.OperationType.PURCHASE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private static final CardMapper cardMapper = CardMapper.INSTANCE;

    @Transactional
    public CardDto addNewCard(CardRequest cardRequest) {

        LocalDate expirationDate = getExpirationDate(cardRequest.getExpirationDate());
        if (expirationDate.isEqual(LocalDate.now()) || expirationDate.isBefore(LocalDate.now())) {
            throw new ExpiredCardException("Expiration date for : " + cardRequest + " is already expired");
        }

        String pan = cardRequest.getPan();
        String cvv = Hashing.sha256().hashString(cardRequest.getCvv(), StandardCharsets.UTF_8).toString();
        Optional<Card> card = cardRepository.findByPanAndCvvAndExpirationDateAndUsername(pan, cvv,
                expirationDate, cardRequest.getUsername());

        if (card.isPresent()) {
            throw new CardAlreadyExistsException("Card with pan " + pan + " and expiration date "
            + cardRequest.getExpirationDate() + " already exists");
        }

        String maskedPan = pan.substring(0,6).concat("*".repeat(6))
                .concat(pan.substring(pan.length() - 4));
        log.info("Masked pan : {} for pan : {} ", maskedPan, pan);

        Card newCard = Card.builder()
                .username(cardRequest.getUsername())
                .pan(pan)
                .maskedPan(maskedPan)
                .expirationDate(expirationDate)
                .cvv(cvv)
                .balance(cardRequest.getBalance())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return cardMapper.toCardDto(cardRepository.saveAndFlush(newCard));
    }


    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
    public CardDto createOperation(OperationRequest operationRequest) {

        LocalDate expirationDate = getExpirationDate(operationRequest.getExpirationDate());

        Card card = cardRepository.findByMaskedPanAndCvvAndExpirationDateAndUsername(operationRequest.getMaskedPan(),
                operationRequest.getCvv(), expirationDate, operationRequest.getUsername())
                .orElseThrow(() -> new CardNotFoundException("No card was found for masked pan "
                        + operationRequest.getMaskedPan()));

        if (card.getExpirationDate().isEqual(LocalDate.now()) || card.getExpirationDate().isBefore(LocalDate.now())) {
            throw new ExpiredCardException("Card " + operationRequest.getMaskedPan() + " / "
                    + operationRequest.getExpirationDate() + " is expired");
        }

        if (operationRequest.getOperationType().equals(PURCHASE)) {


            return cardMapper.toCardDto(cardRepository.save(purchase(card, operationRequest)));

        } else {

            return cardMapper.toCardDto(cardRepository.save(reverse(card, operationRequest)));
        }
    }

    private Card purchase(Card card, OperationRequest operationRequest) {

        if (card.getBalance().compareTo(operationRequest.getPrice()) < 0) {
            throw new CardInsufficientBalanceException("Card " + operationRequest.getMaskedPan()
                    + " balance is lower than " + operationRequest.getPrice());
        }
        log.info("Pay with card : {}", card);
        card.setBalance(card.getBalance().subtract(operationRequest.getPrice()));
        card.setUpdatedAt(LocalDateTime.now());

        return card;
    }


    private Card reverse(Card card, OperationRequest operationRequest) {

        log.info("Pay with card : {}", card);
        card.setBalance(card.getBalance().add(operationRequest.getPrice()));
        card.setUpdatedAt(LocalDateTime.now());

        return card;
    }

    private LocalDate getExpirationDate(String expirationDate) {

        int month = Integer.parseInt(expirationDate.substring(0,2));
        int year = Integer.parseInt(expirationDate.substring(3)) + 2000;
        return LocalDate.of(year, month, 1).plusMonths(1);
    }


    @Transactional
    public List<CardDto> getActiveCards(String username, int page, int pageSize) {

        List<Card> cards = cardRepository.findAllByExpirationDateAfterAndUsername(
                LocalDate.now(), username, PageRequest.of(page, pageSize)).getContent();

        return cardMapper.cardDtos(cards);
    }
}
