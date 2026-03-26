package com.example.userservice.service.impl;

import com.example.userservice.dto.filter.UserFilter;
import com.example.userservice.dto.user.UserCreateDto;
import com.example.userservice.dto.user.UserResponseDto;
import com.example.userservice.dto.user.UserUpdateDto;
import com.example.userservice.entity.User;
import com.example.userservice.exception.AccessDeniedException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.CurrentUserProvider;
import com.example.userservice.service.UserService;
import com.example.userservice.specification.UserSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserSpecification userSpecification;
  private final UserMapper userMapper;
  private final CurrentUserProvider currentUserProvider;

  public UserServiceImpl(UserRepository userRepository, UserSpecification userSpecification, UserMapper userMapper, CurrentUserProvider currentUserProvider) {
    this.userRepository = userRepository;
    this.userSpecification = userSpecification;
    this.userMapper = userMapper;
    this.currentUserProvider = currentUserProvider;
  }

  @Override
  public UserResponseDto create(UserCreateDto dto) {
    User user = userMapper.toEntity(dto);
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  @Cacheable(value = "userWithCards", key = "#userId")
  public UserResponseDto getById(Long userId) {
    Long currentUserId = currentUserProvider.getCurrentUserId();
    String role = currentUserProvider.getCurrentRole();
    if (!role.equals("ROLE_ADMIN") && !currentUserId.equals(userId)) {
      throw new AccessDeniedException();
    }
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    return userMapper.toDto(user);
  }

  @Override
  public Page<UserResponseDto> getAll(UserFilter filter, Pageable pageable) {
    Specification<User> spec = userSpecification.build(filter);
    return userRepository.findAll(spec, pageable)
            .map(userMapper::toDto);
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#id")
  public UserResponseDto update(Long id, UserUpdateDto dto) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    userMapper.updateEntityFromDto(dto, user);
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#id")
  public void activate(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    user.setActive(true);
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#id")
  public void deactivate(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    user.setActive(false);
    user.getCards().forEach(c -> c.setActive(false));
  }
}