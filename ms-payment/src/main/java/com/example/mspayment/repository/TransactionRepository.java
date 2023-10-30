package com.example.mspayment.repository;

import com.example.mspayment.model.Transaction;
import com.example.mspayment.model.TransactionState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByUsernameAndStateIn(String username, List<TransactionState> states, Pageable pageable);

    Optional<Transaction> findByIdAndState(Long transactionId, TransactionState state);
}
