package com.example.mscard.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@IdClass(CardId.class)
public class Card {

    @Column(updatable = false, nullable = false)
    private String username;
    @Column(updatable = false, nullable = false)
    @ToString.Exclude
    private String pan;

    @Id
    @Column(updatable = false, nullable = false)
    private String maskedPan;

    @Id
    @Column(updatable = false, nullable = false)
    private LocalDate expirationDate;

    @Id
    @Column(updatable = false, nullable = false)
    @ToString.Exclude
    private String cvv;

    private BigDecimal balance;

    @Version
    private Long version;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
