package com.example.userservice.dto.user;

import com.example.userservice.dto.paymentсard.PaymentCardResponseDto;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDto(
        Long id,
        String name,
        String surname,
        LocalDate birthDate,
        String email,
        Boolean active,
        List<PaymentCardResponseDto> cards) {
}