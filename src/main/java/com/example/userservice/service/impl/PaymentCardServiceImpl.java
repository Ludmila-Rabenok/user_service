package com.example.userservice.service.impl;

import com.example.userservice.dto.PaymentCardCreateDto;
import com.example.userservice.dto.PaymentCardDto;
import com.example.userservice.dto.PaymentCardUpdateDto;
import com.example.userservice.dto.filter.PaymentCardFilter;
import com.example.userservice.entity.PaymentCard;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.PaymentCardMapper;
import com.example.userservice.repository.PaymentCardRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.PaymentCardService;
import com.example.userservice.specification.PaymentCardSpecification;
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
  public PaymentCardDto create(Long userId, PaymentCardCreateDto dto) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    long cardCount = cardRepository.countByUserId(userId);
    if (cardCount >= 5) {
      throw new RuntimeException("User cannot have more than 5 cards");
    }
    PaymentCard card = cardMapper.toEntity(dto);
    card.setUser(user);
    return cardMapper.toDto(cardRepository.save(card));
  }

  @Override
  public PaymentCardDto getById(Long id) {
    return cardRepository.findById(id)
            .map(cardMapper::toDto)
            .orElseThrow(() -> new RuntimeException("Card not found"));
  }

  @Override
  public List<PaymentCardDto> getByUserId(Long userId) {
    return cardRepository.findCardsByUserId(userId)
            .stream()
            .map(cardMapper::toDto)
            .toList();
  }

  @Override
  public Page<PaymentCardDto> getAll(PaymentCardFilter filter, Pageable pageable) {
    return cardRepository.findAll(
            cardSpecification.build(filter),
            pageable
    ).map(cardMapper::toDto);
  }

  @Override
  @Transactional
  public PaymentCardDto update(Long id, PaymentCardUpdateDto dto) {
    PaymentCard card = cardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Card not found"));
    cardMapper.updateEntityFromDto(dto, card);
    return cardMapper.toDto(cardRepository.save(card));
  }

  @Override
  @Transactional
  public void activate(Long id) {
    if (!cardRepository.existsById(id)) {
      throw new RuntimeException("Card not found");
    }
    cardRepository.updateActiveStatus(id, true);
  }

  @Override
  @Transactional
  public void deactivate(Long id) {
    if (!cardRepository.existsById(id)) {
      throw new RuntimeException("Card not found");
    }
    cardRepository.updateActiveStatus(id, false);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    cardRepository.deleteById(id);
  }
}