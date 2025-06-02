package com.apa.back.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequestDto(String token, @JsonProperty(value = "new_password") @NotNull String newPassword) {}
