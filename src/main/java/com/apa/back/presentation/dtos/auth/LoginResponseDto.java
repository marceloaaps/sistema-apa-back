package com.apa.back.presentation.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto(
        @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Tempo de expiração do token em segundos", example = "3600")
        Long expiresIn
) {}
