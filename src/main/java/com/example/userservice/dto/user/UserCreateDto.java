package com.example.userservice.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserCreateDto(
        @NotBlank String name,
        @NotBlank String surname,
        @NotNull LocalDate birthDate,
        @Email @NotBlank String email) {
}
