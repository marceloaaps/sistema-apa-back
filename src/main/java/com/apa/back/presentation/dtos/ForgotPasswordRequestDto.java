package com.apa.back.presentation.dtos;

import jakarta.validation.constraints.NotNull;

public record ForgotPasswordRequestDto(@NotNull String email) {}
