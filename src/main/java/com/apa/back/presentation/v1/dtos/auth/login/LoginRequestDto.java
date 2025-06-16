package com.apa.back.presentation.v1.dtos.auth.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank
        @Email
        @Schema(description = "Email do usuário para login", example = "usuario@example.com")
        String email,

        @NotBlank
        @Schema(description = "Senha do usuário", example = "Senha123")
        String senha
) {}
