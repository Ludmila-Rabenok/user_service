package com.example.userservice.exception;

public class CardLimitExceededException extends RuntimeException {
  public CardLimitExceededException(Long userId) {
    super("User with id " + userId + " cannot have more than 5 cards");
  }
}