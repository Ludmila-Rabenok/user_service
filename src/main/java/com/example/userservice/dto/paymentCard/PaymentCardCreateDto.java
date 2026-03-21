package com.example.userservice.dto.paymentCard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PaymentCardCreateDto(
        @NotNull
        Long userId,

        @NotBlank
        @Size(min = 16, max = 16)
        String number,

        @NotBlank
        String holder,

        @NotNull
        LocalDate expirationDate) {
}