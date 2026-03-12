package com.example.userservice.mapper;

import com.example.userservice.dto.PaymentCardCreateDto;
import com.example.userservice.dto.PaymentCardDto;
import com.example.userservice.dto.PaymentCardUpdateDto;
import com.example.userservice.entity.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentCardMapper {

  PaymentCardDto toDto(PaymentCard entity);

  PaymentCard toEntity(PaymentCardCreateDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  void updateEntityFromDto(PaymentCardUpdateDto dto, @MappingTarget PaymentCard entity);
}

