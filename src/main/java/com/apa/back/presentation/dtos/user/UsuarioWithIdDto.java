package com.apa.back.presentation.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UsuarioWithIdDto(

        @Schema(description = "Id do usuário", example = "12")
        Long id,

        @Schema(description = "Nome do usuário", example = "Marcelo Silva")
        String nome,

        @Schema(description = "Email do usuário", example = "marcelo@example.com")
        String email,

        @JsonProperty(value = "data_nascimento")
        @Schema(description = "Data de nascimento do usuário no formato ISO", example = "1995-05-20")
        java.time.LocalDate dataNascimento) {
}
