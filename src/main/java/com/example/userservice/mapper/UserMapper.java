package com.example.userservice.mapper;

import com.example.userservice.dto.user.UserCreateDto;
import com.example.userservice.dto.user.UserResponseDto;
import com.example.userservice.dto.user.UserUpdateDto;
import com.example.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = PaymentCardMapper.class)
public interface UserMapper {

  @Mapping(target = "cards", source = "cards")
  UserResponseDto toDto(User user);

  User toEntity(UserCreateDto dto);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(UserUpdateDto dto, @MappingTarget User entity);
}
