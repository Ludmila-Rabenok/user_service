package com.example.userservice.service;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.dto.UserUpdateDto;
import com.example.userservice.dto.filter.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  UserResponseDto create(UserCreateDto userCreateDto);

  UserResponseDto getById(Long id);

  Page<UserResponseDto> getAll(UserFilter filter, Pageable pageable);

  UserResponseDto update(Long id, UserUpdateDto userUpdateDto);

  void activate(Long id);

  void deactivate(Long id);

  void delete(Long id);

}
