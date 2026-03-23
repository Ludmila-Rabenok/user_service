package com.example.userservice.service.impl;

import com.example.userservice.dto.filter.PaymentCardFilter;
import com.example.userservice.dto.paymentcard.PaymentCardCreateDto;
import com.example.userservice.dto.paymentcard.PaymentCardResponseDto;
import com.example.userservice.dto.paymentcard.PaymentCardUpdateDto;
import com.example.userservice.entity.PaymentCard;
import com.example.userservice.entity.User;
import com.example.userservice.exception.CardLimitExceededException;
import com.example.userservice.exception.PaymentCardNotFoundException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.PaymentCardMapper;
import com.example.userservice.repository.PaymentCardRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.specification.PaymentCardSpecification;
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
class PaymentCardServiceImplTest {
  @Mock
  private PaymentCardRepository cardRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PaymentCardSpecification cardSpecification;

  @Mock
  private PaymentCardMapper cardMapper;

  @InjectMocks
  private PaymentCardServiceImpl cardService;

  @Test
  void create_ShouldCreateCard() {
    PaymentCardCreateDto dto = buildCreateDto();
    User user = new User();
    PaymentCard card = new PaymentCard();
    PaymentCardResponseDto expected = buildResponseDto();
    when(userRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(user));
    when(cardRepository.countByUserId(1L)).thenReturn(1);
    when(cardMapper.toEntity(dto)).thenReturn(card);
    when(cardRepository.save(card)).thenReturn(card);
    when(cardMapper.toDto(card)).thenReturn(expected);

    PaymentCardResponseDto actual = cardService.create(dto);

    assertEquals(expected, actual);
    verify(cardRepository).save(card);
  }

  @Test
  void create_ShouldThrow_WhenUserNotFound() {
    PaymentCardCreateDto dto = buildCreateDto();

    when(userRepository.findByIdForUpdate(dto.userId())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> cardService.create(dto));
  }

  @Test
  void create_ShouldThrow_WhenCardLimitExceeded() {
    PaymentCardCreateDto dto = buildCreateDto();
    when(userRepository.findByIdForUpdate(dto.userId())).thenReturn(Optional.of(new User()));
    when(cardRepository.countByUserId(dto.userId())).thenReturn(5);

    assertThrows(CardLimitExceededException.class, () -> cardService.create(dto));
  }

  @Test
  void getById_ShouldReturnDto() {
    PaymentCard card = new PaymentCard();
    PaymentCardResponseDto expected = buildResponseDto();
    when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
    when(cardMapper.toDto(card)).thenReturn(expected);

    PaymentCardResponseDto actual = cardService.getById(1L);

    assertEquals(expected, actual);
    verify(cardRepository).findById(1L);
  }

  @Test
  void getById_ShouldThrow_WhenNotFound() {
    when(cardRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(PaymentCardNotFoundException.class, () -> cardService.getById(1L));
  }

  @Test
  void getByUserId_ShouldReturnList() {
    PaymentCard card = new PaymentCard();
    PaymentCardResponseDto dto = buildResponseDto();
    when(cardRepository.findCardsByUserId(1L)).thenReturn(List.of(card));
    when(cardMapper.toDto(card)).thenReturn(dto);

    List<PaymentCardResponseDto> actual = cardService.getByUserId(1L);

    assertAll(
            () -> assertEquals(1, actual.size()),
            () -> assertEquals(dto, actual.get(0))
    );
  }


  @Test
  void getAll_ShouldReturnPage() {
    PaymentCardFilter filter = mock(PaymentCardFilter.class);
    Pageable pageable = PageRequest.of(0, 10);
    PaymentCard card = new PaymentCard();
    PaymentCardResponseDto dto = buildResponseDto();
    Specification<PaymentCard> spec = (root, query, cb) -> null;
    when(cardSpecification.build(filter)).thenReturn(spec);
    when(cardRepository.findAll(spec, pageable)).thenReturn(new PageImpl<>(List.of(card)));
    when(cardMapper.toDto(card)).thenReturn(dto);

    Page<PaymentCardResponseDto> actual = cardService.getAll(filter, pageable);

    assertAll(
            () -> assertEquals(1, actual.getTotalElements()),
            () -> assertEquals(dto, actual.getContent().get(0))
    );
  }


  @Test
  void update_ShouldUpdateCard() {
    PaymentCardUpdateDto dto = buildUpdateDto();
    PaymentCard card = new PaymentCard();
    PaymentCardResponseDto expected = buildResponseDto();
    when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
    when(cardRepository.save(card)).thenReturn(card);
    when(cardMapper.toDto(card)).thenReturn(expected);

    PaymentCardResponseDto actual = cardService.update(1L, dto);

    assertEquals(expected, actual);
    verify(cardMapper).updateEntityFromDto(dto, card);
  }

  @Test
  void update_ShouldThrow_WhenNotFound() {
    PaymentCardUpdateDto updateDto = buildUpdateDto();
    when(cardRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(PaymentCardNotFoundException.class,
            () -> cardService.update(1L, updateDto));
  }

  @Test
  void activate_ShouldUpdateStatus() {
    PaymentCard paymentCard = new PaymentCard();
    User user = new User();
    user.setId(1L);
    user.setActive(true);
    paymentCard.setUser(user);
    when(cardRepository.findById(1L)).thenReturn(Optional.of(paymentCard));

    Long actual = cardService.activate(1L);

    assertEquals(1L, actual);
  }

  @Test
  void activate_ShouldThrow_WhenNotFound() {
    when(cardRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(PaymentCardNotFoundException.class, () -> cardService.activate(1L));
  }

  @Test
  void deactivate_ShouldUpdateStatus() {
    PaymentCard paymentCard = new PaymentCard();
    User user = new User();
    user.setId(1L);
    paymentCard.setUser(user);
    when(cardRepository.findById(1L)).thenReturn(Optional.of(paymentCard));

    Long actual = cardService.deactivate(1L);

    assertEquals(1L, actual);
  }

  @Test
  void deactivate_ShouldThrow_WhenNotFound() {
    when(cardRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(PaymentCardNotFoundException.class, () -> cardService.deactivate(1L));
  }


  private PaymentCardCreateDto buildCreateDto() {
    return new PaymentCardCreateDto(
            1L,
            "1111111111111111",
            "Ivan Ivanov",
            LocalDate.of(2033, 11, 11)
    );
  }

  private PaymentCardUpdateDto buildUpdateDto() {
    return new PaymentCardUpdateDto(
            "1111111111111111",
            "Ivan Ivanov",
            LocalDate.of(2033, 11, 11),
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