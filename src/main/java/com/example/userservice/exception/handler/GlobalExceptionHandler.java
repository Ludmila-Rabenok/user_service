package com.example.userservice.exception.handler;

import com.example.userservice.dto.error.ErrorResponse;
import com.example.userservice.exception.CardLimitExceededException;
import com.example.userservice.exception.PaymentCardNotFoundException;
import com.example.userservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(PaymentCardNotFoundException.class)
  public ResponseEntity<?> handleCardNotFound(PaymentCardNotFoundException e) {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(CardLimitExceededException.class)
  public ResponseEntity<?> handleCardLimit(CardLimitExceededException e) {
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneral(Exception e) {
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("Unexpected error: " + e.getMessage()));
  }
}