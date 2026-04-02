package com.example.userservice.security;

import com.example.userservice.entity.PaymentCard;
import com.example.userservice.repository.PaymentCardRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CardSecurity {
  private final PaymentCardRepository repository;

  public CardSecurity(PaymentCardRepository repository) {
    this.repository = repository;
  }

  public boolean isOwner(Long cardId) {
    PaymentCard card = repository.findById(cardId).orElse(null);
    if (card == null) return false;
    return card.getUser().getId().equals(
            ((AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
    );
  }
}