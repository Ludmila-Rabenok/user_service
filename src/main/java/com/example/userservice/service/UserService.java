package com.example.userservice.service;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserUpdateDto;
import com.example.userservice.dto.filter.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  UserDto create(UserCreateDto userCreateDto);

  UserDto getById(Long id);

  Page<UserDto> getAll(UserFilter filter, Pageable pageable);

  UserDto update(Long id, UserUpdateDto userUpdateDto);

  void activate(Long id);

  void deactivate(Long id);

  void delete(Long id);

}
