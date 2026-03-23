package com.example.userservice.exception.handler;

import com.example.userservice.dto.error.ErrorResponse;
import com.example.userservice.exception.CardLimitExceededException;
import com.example.userservice.exception.InactiveUserException;
import com.example.userservice.exception.PaymentCardNotFoundException;
import com.example.userservice.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(PaymentCardNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleCardNotFound(PaymentCardNotFoundException e) {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(CardLimitExceededException.class)
  public ResponseEntity<ErrorResponse> handleCardLimit(CardLimitExceededException e) {
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
  }
  @ExceptionHandler(InactiveUserException.class)
  public ResponseEntity<ErrorResponse> handleInactiveUser(InactiveUserException e){
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
  }
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e){
    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("Unexpected error: " + e.getMessage()));
  }
}