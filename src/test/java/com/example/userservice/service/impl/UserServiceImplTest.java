package com.example.userservice.service.impl;

import com.example.userservice.dto.filter.UserFilter;
import com.example.userservice.dto.paymentCard.PaymentCardResponseDto;
import com.example.userservice.dto.user.UserCreateDto;
import com.example.userservice.dto.user.UserResponseDto;
import com.example.userservice.dto.user.UserUpdateDto;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.PaymentCardRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.specification.UserSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserSpecification userSpecification;

  @Mock
  private UserMapper userMapper;

  @Mock
  private PaymentCardRepository cardRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void create_ShouldSaveUserAndReturnDto() {
    UserCreateDto createDto = mock(UserCreateDto.class);
    User user = new User();
    UserResponseDto expected = buildUserResponse();
    when(userMapper.toEntity(createDto)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(expected);

    UserResponseDto actual = userService.create(createDto);

    assertEquals(expected, actual);
    verify(userRepository).save(user);
  }

  @Test
  void getById_ShouldReturnDto() {
    User user = new User();
    UserResponseDto expected = buildUserResponse();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toDto(user)).thenReturn(expected);

    UserResponseDto actual = userService.getById(1L);

    assertEquals(expected, actual);
    verify(userRepository).findById(1L);
  }

  @Test
  void getById_ShouldThrow_WhenNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
            () -> userService.getById(1L));
  }

  @Test
  void getAll_ShouldReturnPageWithDtos() {
    UserFilter filter = mock(UserFilter.class);
    Pageable pageable = PageRequest.of(0, 10);
    User user = new User();
    UserResponseDto expected = buildUserResponse();
    Specification<User> spec = (root, query, cb) -> null;
    when(userSpecification.build(filter)).thenReturn(spec);
    when(userRepository.findAll(spec, pageable)).thenReturn(new PageImpl<>(List.of(user)));
    when(userMapper.toDto(user)).thenReturn(expected);

    Page<UserResponseDto> actual = userService.getAll(filter, pageable);

    assertAll(
            () -> assertEquals(1, actual.getTotalElements()),
            () -> assertEquals(expected, actual.getContent().get(0)));
    verify(userRepository).findAll(spec, pageable);
  }

  @Test
  void update_ShouldUpdateUser() {
    Long id = 1L;
    UserUpdateDto updateDto = buildUpdateDto();
    User user = new User();
    UserResponseDto expected = buildUserResponse();
    when(userRepository.findById(id)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(expected);

    UserResponseDto actual = userService.update(id, updateDto);

    assertEquals(expected, actual);
    verify(userMapper).updateEntityFromDto(updateDto, user);
  }

  @Test
  void update_ShouldThrow_WhenNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
            () -> userService.update(1L, buildUpdateDto()));
  }

  @Test
  void activate_ShouldUpdateStatus() {
    when(userRepository.updateActiveStatus(1L, true)).thenReturn(1);

    userService.activate(1L);

    verify(userRepository).updateActiveStatus(1L, true);
  }

  @Test
  void activate_ShouldThrow_WhenNotFound() {
    when(userRepository.updateActiveStatus(1L, true)).thenReturn(0);

    assertThrows(UserNotFoundException.class,
            () -> userService.activate(1L));
  }

  @Test
  void deactivate_ShouldUpdateStatus() {
    when(userRepository.updateActiveStatus(1L, false)).thenReturn(1);
    when(cardRepository.updateActiveStatusByUserId(1L, false)).thenReturn(1);

    userService.deactivate(1L);

    verify(userRepository).updateActiveStatus(1L, false);
    verify(cardRepository).updateActiveStatusByUserId(1L, false);
  }

  @Test
  void deactivate_ShouldThrow_WhenNotFound() {
    when(userRepository.updateActiveStatus(1L, false)).thenReturn(0);

    assertThrows(UserNotFoundException.class,
            () -> userService.deactivate(1L));
  }

  private UserResponseDto buildUserResponse() {
    return new UserResponseDto(
            1L, "Ivan", "Ivanov",
            LocalDate.of(1999, 1, 1),
            "ivan@example.com",
            true,
            List.of(buildResponseDto())
    );
  }

  private UserUpdateDto buildUpdateDto() {
    return new UserUpdateDto(
            "Ivan", "Ivanov",
            LocalDate.of(1999, 1, 1),
            "ivan@example.com",
            true
    );
  }

  private PaymentCardResponseDto buildResponseDto() {
    return new PaymentCardResponseDto(
            1L,
            "1111111111111111",
            "Ivan Ivanov",
            LocalDate.of(2033, 11, 11),
            true,
            1L
    );
  }
}