package com.example.userservice.mapper;

import com.example.userservice.dto.paymentCard.PaymentCardCreateDto;
import com.example.userservice.dto.paymentCard.PaymentCardResponseDto;
import com.example.userservice.dto.paymentCard.PaymentCardUpdateDto;
import com.example.userservice.entity.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",  uses = PaymentCardMapper.class)
public interface PaymentCardMapper {

  @Mapping(target = "userId", source = "user.id")
  PaymentCardResponseDto toDto(PaymentCard entity);

  PaymentCard toEntity(PaymentCardCreateDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  void updateEntityFromDto(PaymentCardUpdateDto dto, @MappingTarget PaymentCard entity);
}