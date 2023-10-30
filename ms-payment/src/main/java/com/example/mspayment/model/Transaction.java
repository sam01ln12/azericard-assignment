package com.example.mspayment.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_sequence")
    @SequenceGenerator(name = "transaction_sequence", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String username;

    private String maskedPan;

    @Column(nullable = false, updatable = false)
    private String productName;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private TransactionState state;

    private String declineReason;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
