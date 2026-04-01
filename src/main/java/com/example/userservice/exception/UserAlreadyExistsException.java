package com.example.userservice.exception;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(Long userId) {
    super("User with id = [" + userId + "] already exists");
  }
}
