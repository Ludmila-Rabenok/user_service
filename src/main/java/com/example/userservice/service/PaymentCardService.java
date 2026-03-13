package com.example.userservice.service;

import com.example.userservice.dto.PaymentCardCreateDto;
import com.example.userservice.dto.PaymentCardResponseDto;
import com.example.userservice.dto.PaymentCardUpdateDto;
import com.example.userservice.dto.filter.PaymentCardFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentCardService {
  PaymentCardResponseDto create(PaymentCardCreateDto dto);

  PaymentCardResponseDto getById(Long id);

  List<PaymentCardResponseDto> getByUserId(Long userId);

  Page<PaymentCardResponseDto> getAll(PaymentCardFilter filter, Pageable pageable);

  PaymentCardResponseDto update(Long id, PaymentCardUpdateDto dto);

  void activate(Long id);

  void deactivate(Long id);

  void delete(Long id);

}
