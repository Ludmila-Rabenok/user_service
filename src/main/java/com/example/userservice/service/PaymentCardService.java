package com.example.userservice.service;

import com.example.userservice.dto.PaymentCardCreateDto;
import com.example.userservice.dto.PaymentCardDto;
import com.example.userservice.dto.PaymentCardUpdateDto;
import com.example.userservice.dto.filter.PaymentCardFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentCardService {
  PaymentCardDto create(Long userId, PaymentCardCreateDto dto);

  PaymentCardDto getById(Long id);

  List<PaymentCardDto> getByUserId(Long userId);

  Page<PaymentCardDto> getAll(PaymentCardFilter filter, Pageable pageable);

  PaymentCardDto update(Long id, PaymentCardUpdateDto dto);

  void activate(Long id);

  void deactivate(Long id);

  void delete(Long id);

}
