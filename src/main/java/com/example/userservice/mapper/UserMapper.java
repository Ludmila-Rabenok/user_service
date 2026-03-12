package com.example.userservice.mapper;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserUpdateDto;
import com.example.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto toDto(User entity);

  User toEntity(UserCreateDto dto);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(UserUpdateDto dto, @MappingTarget User entity);
}
