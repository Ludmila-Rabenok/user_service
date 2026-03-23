package com.example.userservice.service;

import com.example.userservice.dto.user.UserCreateDto;
import com.example.userservice.dto.user.UserResponseDto;
import com.example.userservice.dto.user.UserUpdateDto;
import com.example.userservice.dto.filter.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  UserResponseDto create(UserCreateDto userCreateDto);

  UserResponseDto getById(Long userId);

  Page<UserResponseDto> getAll(UserFilter filter, Pageable pageable);

  UserResponseDto update(Long id, UserUpdateDto userUpdateDto);

  void activate(Long id);

  void deactivate(Long id);

}
