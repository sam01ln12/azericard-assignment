package com.example.mscard.repository;

import com.example.mscard.model.Card;
import com.example.mscard.model.CardId;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, CardId> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Card> findByMaskedPanAndCvvAndExpirationDateAndUsername(
            String maskedPan, String cvv, LocalDate expirationDate, String username);

    Optional<Card> findByPanAndCvvAndExpirationDateAndUsername(
            String pan, String cvv, LocalDate expirationDate, String username);

    Page<Card> findAllByExpirationDateAfterAndUsername(LocalDate  today, String username, Pageable pageable);
}
