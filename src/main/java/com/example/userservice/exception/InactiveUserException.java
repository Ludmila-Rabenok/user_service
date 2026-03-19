package com.example.userservice.exception;

public class InactiveUserException extends RuntimeException {

  public InactiveUserException(Long userId) {
    super("Cannot activate card because user " + userId + " is inactive");
  }
}