package com.example.mspayment.service;

import com.example.mspayment.config.client.CardClient;
import com.example.mspayment.config.client.ProductClient;
import com.example.mspayment.error.CommonException;
import com.example.mspayment.error.IncorrectPaymentDataException;
import com.example.mspayment.error.TransactionNotFoundException;
import com.example.mspayment.model.*;
import com.example.mspayment.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CardClient mockCardClient;
    @Mock
    private ProductClient mockProductClient;
    @Mock
    private TransactionRepository mockTransactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(mockCardClient, mockProductClient,
                mockTransactionRepository);
    }

    @Test
    void testGetAllTransactionByStates() {

        final TransactionRequest transactionRequest = new TransactionRequest("username",
                List.of(TransactionState.CREATED), 0, 1);
        final List<TransactionDto> expectedResult = List.of(TransactionDto.builder().build());

        final Page<Transaction> transactions = new PageImpl<>(List.of(Transaction.builder()
                .username("username")
                .maskedPan("maskedPan")
                .productName("productName")
                .price(new BigDecimal("0.00"))
                .state(TransactionState.CREATED)
                .expirationDate("expirationDate")
                .cvv("cvv")
                .declineReason("declineReason")
                .createdAt(LocalDate.of(2020, 1, 1))
                .updatedAt(LocalDate.of(2020, 1, 1))
                .build()));
        when(mockTransactionRepository.findAllByUsernameAndStateIn("username", List.of(TransactionState.CREATED),
                PageRequest.of(0, 1))).thenReturn(transactions);

        final List<TransactionDto> result = transactionService.getAllTransactionByStates(transactionRequest);
        assertEquals(1, result.size());
    }

    @Test
    void testPurchaseProductIncorrectPaymentDataException1() {

        final PurchaseRequest purchaseRequest = new PurchaseRequest("maskedPan", "productName");
        assertThrows(IncorrectPaymentDataException.class, () ->
                transactionService.purchaseProduct(purchaseRequest, "paymentData"));
    }

    @Test
    void testPurchaseProductIncorrectPaymentDataException2() {
        String paymentData = new String(Base64.getEncoder().encode("username:cvv:01/25".getBytes()));
        final PurchaseRequest purchaseRequest = new PurchaseRequest("maskedPan", "productName");
        assertThrows(IncorrectPaymentDataException.class, () ->
                transactionService.purchaseProduct(purchaseRequest, paymentData));
    }

    @Test
    void testPurchaseProduct() {

        String paymentData = new String(Base64.getEncoder().encode("username:111:01/25".getBytes()));
        final PurchaseRequest purchaseRequest = new PurchaseRequest("maskedPan", "productName");

        final ProductDto productDto = new ProductDto("productName", new BigInteger("100"), BigDecimal.ONE);
        when(mockProductClient.buyProduct("productName")).thenReturn(productDto);

        transactionService.purchaseProduct(purchaseRequest, paymentData);

        verify(mockCardClient).createOperation(OperationRequest.builder()
                .username("username")
                .maskedPan("maskedPan")
                .expirationDate("01/25")
                .cvv("f6e0a1e2ac41945a9aa7ff8a8aaa0cebc12a3bcc981a929ad5cf810a090e11ae")
                .price(BigDecimal.ONE)
                .operationType(OperationType.PURCHASE)
                .build());
        verify(mockTransactionRepository).save(any(Transaction.class));
    }

    @Test
    void testPurchaseProductCommonExceptionProduct() {

        String paymentData = new String(Base64.getEncoder().encode("username:111:01/25".getBytes()));
        final PurchaseRequest purchaseRequest = new PurchaseRequest("maskedPan", "productName");

        when(mockProductClient.buyProduct("productName")).thenThrow(CommonException.class);

        assertThrows(CommonException.class, () -> transactionService.purchaseProduct(purchaseRequest, paymentData));

        verify(mockTransactionRepository).save(any(Transaction.class));
    }

    @Test
    void testPurchaseProductExceptionProduct() {

        String paymentData = new String(Base64.getEncoder().encode("username:111:01/25".getBytes()));
        final PurchaseRequest purchaseRequest = new PurchaseRequest("maskedPan", "productName");

        when(mockProductClient.buyProduct("productName")).thenThrow(new IndexOutOfBoundsException("exception"));

        assertThrows(CommonException.class, () -> transactionService.purchaseProduct(purchaseRequest, paymentData));

        verify(mockTransactionRepository).save(any(Transaction.class));
    }

    @Test
    void testPurchaseProductCommonExceptionCard() {

        String paymentData = new String(Base64.getEncoder().encode("username:111:01/25".getBytes()));
        final PurchaseRequest purchaseRequest = new PurchaseRequest("maskedPan", "productName");

        final ProductDto productDto = new ProductDto("productName", new BigInteger("100"), BigDecimal.ONE);
        when(mockProductClient.buyProduct("productName")).thenReturn(productDto);
        when(mockCardClient.createOperation(any(OperationRequest.class))).thenThrow(CommonException.class);
        transactionService.purchaseProduct(purchaseRequest, paymentData);

        verify(mockProductClient).reverseProduct("productName");
        verify(mockTransactionRepository).save(any(Transaction.class));
    }

    @Test
    void testPurchaseProductExceptionCard() {

        String paymentData = new String(Base64.getEncoder().encode("username:111:01/25".getBytes()));
        final PurchaseRequest purchaseRequest = new PurchaseRequest("maskedPan", "productName");

        final ProductDto productDto = new ProductDto("productName", new BigInteger("100"), BigDecimal.ONE);
        when(mockProductClient.buyProduct("productName")).thenReturn(productDto);
        when(mockCardClient.createOperation(any(OperationRequest.class))).thenThrow(new IndexOutOfBoundsException("exception"));
        transactionService.purchaseProduct(purchaseRequest, paymentData);

        verify(mockProductClient).reverseProduct("productName");
        verify(mockTransactionRepository).save(any(Transaction.class));
    }

    @Test
    void testReverseProduct() {

        final Optional<Transaction> transaction = Optional.of(Transaction.builder()
                .username("username")
                .maskedPan("maskedPan")
                .productName("productName")
                .price(BigDecimal.ONE)
                .state(TransactionState.APPROVED)
                .expirationDate("01/25")
                .cvv("f6e0a1e2ac41945a9aa7ff8a8aaa0cebc12a3bcc981a929ad5cf810a090e11ae")
                .build());
        when(mockTransactionRepository.findByIdAndState(0L, TransactionState.APPROVED)).thenReturn(transaction);

        transactionService.reverseProduct(0L);

        verify(mockCardClient).createOperation(OperationRequest.builder()
                .username("username")
                .maskedPan("maskedPan")
                .expirationDate("01/25")
                .cvv("f6e0a1e2ac41945a9aa7ff8a8aaa0cebc12a3bcc981a929ad5cf810a090e11ae")
                .price(BigDecimal.ONE)
                .operationType(OperationType.REVERSE)
                .build());
        verify(mockProductClient).reverseProduct("productName");
    }

    @Test
    void testReverseProductThrowsTransactionNotFoundException() {

        when(mockTransactionRepository.findByIdAndState(0L, TransactionState.APPROVED)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.reverseProduct(0L));
    }
}
