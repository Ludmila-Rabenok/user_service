package com.example.userservice.dto.user;

import com.example.userservice.dto.paymentCard.PaymentCardResponseDto;

import java.util.List;

public record UserWithCardsResponseDto(
        Long id,
        String name,
        String email,
        boolean active,
        List<PaymentCardResponseDto> cards) {
}