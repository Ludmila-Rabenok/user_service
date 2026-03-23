package com.example.userservice.service.impl;

import com.example.userservice.dto.paymentсard.PaymentCardCreateDto;
import com.example.userservice.dto.paymentсard.PaymentCardResponseDto;
import com.example.userservice.dto.paymentсard.PaymentCardUpdateDto;
import com.example.userservice.dto.filter.PaymentCardFilter;
import com.example.userservice.entity.PaymentCard;
import com.example.userservice.entity.User;
import com.example.userservice.exception.CardLimitExceededException;
import com.example.userservice.exception.InactiveUserException;
import com.example.userservice.exception.PaymentCardNotFoundException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.PaymentCardMapper;
import com.example.userservice.repository.PaymentCardRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.PaymentCardService;
import com.example.userservice.specification.PaymentCardSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentCardServiceImpl implements PaymentCardService {
  private final PaymentCardRepository cardRepository;
  private final UserRepository userRepository;
  private final PaymentCardSpecification cardSpecification;
  private final PaymentCardMapper cardMapper;

  public PaymentCardServiceImpl(PaymentCardRepository cardRepository, UserRepository userRepository, PaymentCardSpecification cardSpecification, PaymentCardMapper cardMapper) {
    this.cardRepository = cardRepository;
    this.userRepository = userRepository;
    this.cardSpecification = cardSpecification;
    this.cardMapper = cardMapper;
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#dto.userId()")
  public PaymentCardResponseDto create(PaymentCardCreateDto dto) {
    User user = userRepository.findByIdForUpdate(dto.userId())
            .orElseThrow(() -> new UserNotFoundException(dto.userId()));
    long cardCount = cardRepository.countByUserId(dto.userId());
    if (cardCount >= 5) {
      throw new CardLimitExceededException(dto.userId());
    }
    PaymentCard card = cardMapper.toEntity(dto);
    card.setUser(user);
    return cardMapper.toDto(cardRepository.save(card));
  }

  @Override
  public PaymentCardResponseDto getById(Long id) {
    return cardRepository.findById(id)
            .map(cardMapper::toDto)
            .orElseThrow(() -> new PaymentCardNotFoundException(id));
  }

  @Override
  public List<PaymentCardResponseDto> getByUserId(Long userId) {
    return cardRepository.findCardsByUserId(userId)
            .stream()
            .map(cardMapper::toDto)
            .toList();
  }

  @Override
  public Page<PaymentCardResponseDto> getAll(PaymentCardFilter filter, Pageable pageable) {
    return cardRepository.findAll(
            cardSpecification.build(filter),
            pageable
    ).map(cardMapper::toDto);
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#result.userId")
  public PaymentCardResponseDto update(Long id, PaymentCardUpdateDto dto) {
    PaymentCard card = cardRepository.findById(id)
            .orElseThrow(() -> new PaymentCardNotFoundException(id));
    cardMapper.updateEntityFromDto(dto, card);
    return cardMapper.toDto(cardRepository.save(card));
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#result")
  public Long activate(Long id) {
    PaymentCard card = cardRepository.findById(id)
            .orElseThrow(() -> new PaymentCardNotFoundException(id));
    if (!card.getUser().isActive()) {
      throw new InactiveUserException(card.getUser().getId());
    }
    card.setActive(true);
    return card.getUser().getId();
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#result")
  public Long deactivate(Long id) {
    PaymentCard card = cardRepository.findById(id)
            .orElseThrow(() -> new PaymentCardNotFoundException(id));
    card.setActive(false);
    return card.getUser().getId();
  }

}