package com.example.userservice.controller;

import com.example.userservice.dto.paymentCard.PaymentCardCreateDto;
import com.example.userservice.dto.paymentCard.PaymentCardResponseDto;
import com.example.userservice.dto.paymentCard.PaymentCardUpdateDto;
import com.example.userservice.dto.filter.PaymentCardFilter;
import com.example.userservice.service.PaymentCardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class PaymentCardController {
  private final PaymentCardService cardService;

  public PaymentCardController(PaymentCardService cardService) {
    this.cardService = cardService;
  }

  @GetMapping
  public ResponseEntity<Page<PaymentCardResponseDto>> getAll(PaymentCardFilter filter, Pageable pageable) {
    return ResponseEntity.ok(cardService.getAll(filter, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentCardResponseDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(cardService.getById(id));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<PaymentCardResponseDto>> getByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(cardService.getByUserId(userId));
  }

  @PostMapping
  public ResponseEntity<PaymentCardResponseDto> create(@RequestBody PaymentCardCreateDto dto) {
    PaymentCardResponseDto created = cardService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PaymentCardResponseDto> update(@PathVariable Long id,
                                                       @RequestBody PaymentCardUpdateDto dto) {
    return ResponseEntity.ok(cardService.update(id, dto));
  }

  @PatchMapping("/{id}/activate")
  public ResponseEntity<String> activate(@PathVariable Long id) {
    Long userId = cardService.activate(id);
    return ResponseEntity.ok("Карта пользователя с id " + userId + " активирована");
  }

  @PatchMapping("/{id}/deactivate")
  public ResponseEntity<String> deactivate(@PathVariable Long id) {
    Long userId = cardService.deactivate(id);
    return ResponseEntity.ok("Карта пользователя с id " + userId + " деактивирована");
  }
}