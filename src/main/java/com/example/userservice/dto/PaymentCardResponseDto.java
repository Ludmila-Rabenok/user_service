package com.example.userservice.dto;

import java.time.LocalDate;

public record PaymentCardResponseDto(
        Long id,
        String number,
        String holder,
        LocalDate expirationDate,
        Boolean active,
        Long userId) {
}
