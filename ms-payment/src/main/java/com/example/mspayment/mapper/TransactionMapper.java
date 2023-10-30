package com.example.mspayment.mapper;

import com.example.mspayment.model.Transaction;
import com.example.mspayment.model.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "transactionId", source = "id")
    TransactionDto toTransactionDto(Transaction transaction);

    List<TransactionDto> dtos(List<Transaction> transactions);
}
