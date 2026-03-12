package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PaymentCardCreateDto(
        @NotBlank
        @Size(min = 16, max = 16)
        String cardNumber,

        @NotBlank
        String cardHolder,

        @NotNull
        LocalDate expirationDate) {
}
