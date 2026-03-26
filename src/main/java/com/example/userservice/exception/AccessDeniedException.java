package com.example.userservice.exception;

public class AccessDeniedException extends RuntimeException{
  public AccessDeniedException() {
    super("You cannot access another user's data");
  }
}