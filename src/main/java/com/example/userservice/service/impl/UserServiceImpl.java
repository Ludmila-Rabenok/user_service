package com.example.userservice.service.impl;

import com.example.userservice.dto.filter.UserFilter;
import com.example.userservice.dto.user.UserCreateDto;
import com.example.userservice.dto.user.UserResponseDto;
import com.example.userservice.dto.user.UserUpdateDto;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.AuthUserDetails;
import com.example.userservice.service.UserService;
import com.example.userservice.specification.UserSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
  @Transactional
  @PreAuthorize("isAuthenticated()")
  public UserResponseDto create(UserCreateDto dto) {
    Long userId = ((AuthUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal()).getId();
    if (userRepository.existsById(userId)) {
      throw new UserAlreadyExistsException(userId);
    }
    User user = userMapper.toEntity(dto);
    user.setId(userId);
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  @Cacheable(value = "userWithCards", key = "#userId")
  @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
  public UserResponseDto getById(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    return userMapper.toDto(user);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public Page<UserResponseDto> getAll(UserFilter filter, Pageable pageable) {
    Specification<User> spec = userSpecification.build(filter);
    return userRepository.findAll(spec, pageable)
            .map(userMapper::toDto);
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#id")
  @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
  public UserResponseDto update(Long id, UserUpdateDto dto) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    userMapper.updateEntityFromDto(dto, user);
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#id")
  @PreAuthorize("hasRole('ADMIN')")
  public void activate(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    user.setActive(true);
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#id")
  @PreAuthorize("hasRole('ADMIN')")
  public void deactivate(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    user.setActive(false);
    user.getCards().forEach(c -> c.setActive(false));
  }
}