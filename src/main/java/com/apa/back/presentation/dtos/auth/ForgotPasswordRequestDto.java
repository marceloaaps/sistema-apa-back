package com.apa.back.presentation.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ForgotPasswordRequestDto(
        @NotNull
        @Schema(description = "Email para envio do token de redefinição", example = "usuario@example.com")
        String email
) {}
