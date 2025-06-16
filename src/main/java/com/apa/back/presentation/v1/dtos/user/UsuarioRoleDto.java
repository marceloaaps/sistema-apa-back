package com.apa.back.presentation.v1.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRoleDto(@Schema(description = "Role do usuário a ser atualizado", example = "ADMIN")
                              @NotBlank String role) {
}
