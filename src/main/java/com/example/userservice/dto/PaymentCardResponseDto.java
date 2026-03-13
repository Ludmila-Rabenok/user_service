package com.example.userservice.dto;

import java.time.LocalDate;

public record PaymentCardResponseDto(
        Long id,
        String cardNumber,
        String cardHolder,
        LocalDate expirationDate,
        Boolean active,
        Long userId) {
}
