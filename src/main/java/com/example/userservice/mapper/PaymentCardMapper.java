package com.example.userservice.mapper;

import com.example.userservice.dto.paymentcard.PaymentCardCreateDto;
import com.example.userservice.dto.paymentcard.PaymentCardResponseDto;
import com.example.userservice.dto.paymentcard.PaymentCardUpdateDto;
import com.example.userservice.entity.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentCardMapper {

  @Mapping(target = "userId", source = "user.id")
  PaymentCardResponseDto toDto(PaymentCard entity);

  List<PaymentCardResponseDto> toDtoList(List<PaymentCard> cards);

  PaymentCard toEntity(PaymentCardCreateDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  void updateEntityFromDto(PaymentCardUpdateDto dto, @MappingTarget PaymentCard entity);
}