package com.apa.back.presentation.v1.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

// Sofrer refatoração no futuro, pra ENUM
public record UsuarioRoleDto(@Schema(description = "Role do usuário a ser atualizado", example = "ADMIN")
                              @NotBlank String role) {
}
