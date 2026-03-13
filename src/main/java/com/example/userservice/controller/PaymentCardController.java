package com.example.userservice.controller;

import com.example.userservice.dto.PaymentCardCreateDto;
import com.example.userservice.dto.PaymentCardResponseDto;
import com.example.userservice.dto.PaymentCardUpdateDto;
import com.example.userservice.dto.filter.PaymentCardFilter;
import com.example.userservice.service.PaymentCardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    cardService.delete(id);
    return ResponseEntity.noContent().build();
  }
}