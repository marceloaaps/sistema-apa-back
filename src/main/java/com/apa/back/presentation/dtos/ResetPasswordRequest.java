package com.apa.back.presentation.dtos;

import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequest(@NotNull String token, @NotNull String newPassword) {}
