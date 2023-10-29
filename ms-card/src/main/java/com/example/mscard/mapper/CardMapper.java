package com.example.mscard.mapper;

import com.example.mscard.model.Card;
import com.example.mscard.model.CardDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);
    CardDto toCardDto(Card card);

    List<CardDto> cardDtos(List<Card> cards);


}
