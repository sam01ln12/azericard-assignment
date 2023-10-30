package com.example.mspayment.service;

import com.example.mspayment.config.client.CardClient;
import com.example.mspayment.config.client.ProductClient;
import com.example.mspayment.error.CommonException;
import com.example.mspayment.error.ErrorCodes;
import com.example.mspayment.error.IncorrectPaymentDataException;
import com.example.mspayment.error.TransactionNotFoundException;
import com.example.mspayment.mapper.TransactionMapper;
import com.example.mspayment.model.*;
import com.example.mspayment.repository.TransactionRepository;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final CardClient cardClient;
    private final ProductClient productClient;
    private final TransactionRepository transactionRepository;
    private static final TransactionMapper transactionMapper = TransactionMapper.INSTANCE;

    @Transactional
    public List<TransactionDto> getAllTransactionByStates(TransactionRequest transactionRequest) {

        List<Transaction> transactions = transactionRepository.findAllByUsernameAndStateIn(transactionRequest.getUsername(),
                transactionRequest.getStates(),
                PageRequest.of(transactionRequest.getPageNumber(), transactionRequest.getPageSize())).getContent();

        return transactionMapper.dtos(transactions);
    }

    @Transactional
    public TransactionDto purchaseProduct(PurchaseRequest purchaseRequest, String paymentData) {

        OperationRequest operationRequest = parsePaymentData(paymentData);
        operationRequest.setOperationType(OperationType.PURCHASE);
        operationRequest.setMaskedPan(purchaseRequest.getMaskedPan());

        Transaction transaction = Transaction.builder()
                .username(operationRequest.getUsername())
                .maskedPan(purchaseRequest.getMaskedPan())
                .productName(purchaseRequest.getProductName())
                .state(TransactionState.CREATED)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        log.info("Transaction before product : {}", transaction);
        buyProduct(transaction);
        log.info("Transaction after product : {}", transaction);

        log.info("Transaction before purchase : {}", transaction);
        operationRequest.setPrice(transaction.getPrice());
        purchaseProduct(transaction, operationRequest);
        log.info("Transaction after purchase : {}", transaction);

        transactionRepository.save(transaction);

        return transactionMapper.toTransactionDto(transaction);

    }

    @Transactional
    public TransactionDto reverseProduct(Long transactionId) {

        Transaction transaction = transactionRepository.findByIdAndState(transactionId, TransactionState.APPROVED)
                .orElseThrow(() -> new TransactionNotFoundException("Approved transaction for id " + transactionId + " not found"));

        OperationRequest operationRequest = OperationRequest.builder()
                .operationType(OperationType.REVERSE)
                .expirationDate(transaction.getExpirationDate())
                .price(transaction.getPrice())
                .cvv(transaction.getCvv())
                .username(transaction.getUsername())
                .maskedPan(transaction.getMaskedPan())
                .build();

        cardClient.createOperation(operationRequest);
        productClient.reverseProduct(transaction.getProductName());
        transaction.setState(TransactionState.REVERSED);
        transaction.setUpdatedAt(LocalDate.now());

        return transactionMapper.toTransactionDto(transactionRepository.save(transaction));
    }

    private void buyProduct(Transaction transaction) {

        ProductDto product;
        try {
            product = productClient.buyProduct(transaction.getProductName());
            transaction.setPrice(product.getPrice());
        } catch (CommonException ex) {

            log.error("Common Exception occurred while trying to buy product : {}, code : {}, message : {}" ,
                    transaction.getProductName(), ex.getErrorCode(), ex.getErrorMessage());

            transaction.setDeclineReason(ex.getErrorMessage());
            transaction.setState(TransactionState.DECLINED);
            transaction.setUpdatedAt(LocalDate.now());
            transactionRepository.save(transaction);

            throw ex;

        } catch (Exception ex) {

            log.error("General Exception occurred while trying to buy product : {},  message : {}" ,
                    transaction.getProductName(), ex.getMessage());
            transaction.setDeclineReason(ex.getMessage());
            transaction.setState(TransactionState.ERROR);
            transaction.setUpdatedAt(LocalDate.now());

            transactionRepository.save(transaction);
            throw new CommonException(ErrorCodes.UNEXPECTED_ERROR, ex.getMessage());
        }
    }

    private void purchaseProduct(Transaction transaction, OperationRequest operationRequest) {

        try {
            cardClient.createOperation(operationRequest);

            transaction.setState(TransactionState.APPROVED);
            transaction.setExpirationDate(operationRequest.getExpirationDate());
            transaction.setCvv(operationRequest.getCvv());
            transaction.setUpdatedAt(LocalDate.now());

        } catch (CommonException ex) {

            log.error("Common Exception occurred while trying to buy product : {}, code : {}, message : {}" ,
                    transaction.getProductName(), ex.getErrorCode(), ex.getErrorMessage());
            log.info("Reverse product : {}", transaction.getProductName());
            productClient.reverseProduct(transaction.getProductName());

            transaction.setState(TransactionState.REVERSED);
            transaction.setUpdatedAt(LocalDate.now());

        } catch (Exception ex) {

            log.error("General Exception occurred while trying to buy product : {},  message : {}" ,
                    transaction.getProductName(), ex.getMessage());
            log.info("Reverse product : {}", transaction.getProductName());
            productClient.reverseProduct(transaction.getProductName());
            transaction.setState(TransactionState.REVERSED);
            transaction.setUpdatedAt(LocalDate.now());
        }
    }


    private OperationRequest parsePaymentData(String paymentData) {
        String[] data = new String(Base64.getDecoder().decode(paymentData)).split(":");

        final long count = Arrays.stream(data).filter(StringUtils::isNotEmpty).count();

        if (count != 3) {
            throw new IncorrectPaymentDataException("X-Payment-Data is not correct");
        }

        if (!data[1].matches("\\d{3}") || !data[2].matches("(?:0[1-9]|1[0-2])/[0-9]{2}")) {
            throw new IncorrectPaymentDataException("Card data is not correct");
        }

        return OperationRequest.builder()
                .username((data[0]))
                .cvv(Hashing.sha256().hashString(data[1], StandardCharsets.UTF_8).toString())
                .expirationDate(data[2])
                .build();
    }
}
