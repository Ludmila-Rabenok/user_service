package com.example.userservice.mapper;

import com.example.userservice.dto.user.UserCreateDto;
import com.example.userservice.dto.user.UserResponseDto;
import com.example.userservice.dto.user.UserUpdateDto;
import com.example.userservice.dto.user.UserWithCardsResponseDto;
import com.example.userservice.entity.PaymentCard;
import com.example.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserResponseDto toDto(User entity);

  @Mapping(target = "cards", source = "cards")
  UserWithCardsResponseDto toUserWithCardsDto(User user, List<PaymentCard> cards);

  User toEntity(UserCreateDto dto);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(UserUpdateDto dto, @MappingTarget User entity);
}
