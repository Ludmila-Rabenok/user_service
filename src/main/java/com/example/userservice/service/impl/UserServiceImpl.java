package com.example.userservice.service.impl;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserUpdateDto;
import com.example.userservice.dto.filter.UserFilter;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import com.example.userservice.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserSpecification userSpecification;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepository userRepository, UserSpecification userSpecification, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userSpecification = userSpecification;
    this.userMapper = userMapper;
  }

  @Override
  public UserDto create(UserCreateDto dto) {
    User user = userMapper.toEntity(dto);
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  public UserDto getById(Long id) {
    return userRepository.findById(id)
            .map(userMapper::toDto)
            .orElseThrow(() -> new RuntimeException("User not found"));
  }

  @Override
  public Page<UserDto> getAll(UserFilter filter, Pageable pageable) {
    return userRepository.findAll(
            userSpecification.build(filter),
            pageable
    ).map(userMapper::toDto);
  }

  @Override
  @Transactional
  public UserDto update(Long id, UserUpdateDto dto) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    userMapper.updateEntityFromDto(dto, user);
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  @Transactional
  public void activate(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    user.setActive(true);
  }

  @Override
  @Transactional
  public void deactivate(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    user.setActive(false);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    userRepository.deleteById(id);
  }

}
