package com.example.msproduct.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String productName;

    private BigInteger stock;
    private BigDecimal price;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
