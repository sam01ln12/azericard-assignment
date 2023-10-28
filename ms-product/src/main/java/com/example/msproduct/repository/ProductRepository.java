package com.example.msproduct.repository;

import com.example.msproduct.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Product> findProductByProductName(String productName);

    Page<Product> findAllByStockGreaterThan(BigInteger stock, Pageable pageable);
}
