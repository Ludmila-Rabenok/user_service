package com.example.userservice.service.impl;

import com.example.userservice.dto.filter.PaymentCardFilter;
import com.example.userservice.dto.paymentcard.PaymentCardCreateDto;
import com.example.userservice.dto.paymentcard.PaymentCardResponseDto;
import com.example.userservice.dto.paymentcard.PaymentCardUpdateDto;
import com.example.userservice.entity.PaymentCard;
import com.example.userservice.entity.User;
import com.example.userservice.exception.CardLimitExceededException;
import com.example.userservice.exception.InactiveUserException;
import com.example.userservice.exception.PaymentCardNotFoundException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.PaymentCardMapper;
import com.example.userservice.repository.PaymentCardRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.AuthUserDetails;
import com.example.userservice.service.PaymentCardService;
import com.example.userservice.specification.PaymentCardSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
  @CacheEvict(value = "userWithCards", key = "#result.userId()")
  @PreAuthorize("isAuthenticated()")
  public PaymentCardResponseDto create(PaymentCardCreateDto dto) {
    Long userId = ((AuthUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal()).getId();
    User user = userRepository.findByIdForUpdate(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    long cardCount = cardRepository.countByUserId(userId);
    if (cardCount >= 5) {
      throw new CardLimitExceededException(userId);
    }
    PaymentCard card = cardMapper.toEntity(dto);
    card.setUser(user);
    return cardMapper.toDto(cardRepository.save(card));
  }

  @Override
  @PreAuthorize("@cardSecurity.isOwner(#id) or hasRole('ADMIN')")
  public PaymentCardResponseDto getById(Long id) {
    return cardRepository.findById(id)
            .map(cardMapper::toDto)
            .orElseThrow(() -> new PaymentCardNotFoundException(id));
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public List<PaymentCardResponseDto> getUserCards() {
    Long userId = ((AuthUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal()).getId();
    return cardRepository.findCardsByUserId(userId)
            .stream()
            .map(cardMapper::toDto)
            .toList();
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public Page<PaymentCardResponseDto> getAll(PaymentCardFilter filter, Pageable pageable) {
    return cardRepository.findAll(
            cardSpecification.build(filter),
            pageable
    ).map(cardMapper::toDto);
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#result.userId")
  @PreAuthorize("@cardSecurity.isOwner(#id) or hasRole('ADMIN')")
  public PaymentCardResponseDto update(Long id, PaymentCardUpdateDto dto) {
    PaymentCard card = cardRepository.findById(id)
            .orElseThrow(() -> new PaymentCardNotFoundException(id));
    cardMapper.updateEntityFromDto(dto, card);
    return cardMapper.toDto(cardRepository.save(card));
  }

  @Override
  @Transactional
  @CacheEvict(value = "userWithCards", key = "#result")
  @PreAuthorize("@cardSecurity.isOwner(#id) or hasRole('ADMIN')")
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
  @PreAuthorize("@cardSecurity.isOwner(#id) or hasRole('ADMIN')")
  public Long deactivate(Long id) {
    PaymentCard card = cardRepository.findById(id)
            .orElseThrow(() -> new PaymentCardNotFoundException(id));
    card.setActive(false);
    return card.getUser().getId();
  }
}