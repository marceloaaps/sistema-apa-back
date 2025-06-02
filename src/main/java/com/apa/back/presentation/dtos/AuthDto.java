package com.apa.back.presentation.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AuthDto(
        @NotBlank
        @Schema(description = "Nome completo do usuário", example = "Marcelo Silva")
        String nome,

        @NotBlank
        @Email
        @Schema(description = "Email válido do usuário", example = "marcelo@example.com")
        String email,

        @NotNull
        @Past
        @Schema(description = "Data de nascimento do usuário (deve ser passada no passado)", example = "1995-05-20")
        LocalDate dataNascimento,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, com letras maiúsculas, minúsculas e números."
        )
        @Schema(description = "Senha com ao menos 8 caracteres, letras maiúsculas, minúsculas e números", example = "Senha123")
        String senha
) {}
