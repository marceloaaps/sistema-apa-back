package com.apa.back.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsuarioDto(String nome, String email, @JsonProperty(value = "data_nascimento") String dataNascimento) {
}
