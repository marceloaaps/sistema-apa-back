package com.apa.back.presentation.v1.dtos.auth.password;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequestDto(
        @Schema(description = "Token enviado por email para validação da redefinição", example = "token1234")
        String token,

        @JsonProperty(value = "new_password")
        @NotNull
        @Schema(description = "Nova senha do usuário", example = "NovaSenha123")
        String newPassword
) {}
