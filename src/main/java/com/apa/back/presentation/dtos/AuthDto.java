package com.apa.back.presentation.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;


public record AuthDto(@NotBlank String nome, @Email String email, @NotNull @Past LocalDate dataNascimento, @NotBlank String senha) {
}
