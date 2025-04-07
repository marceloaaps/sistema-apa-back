package com.apa.back.presentation.dtos;


import jakarta.validation.constraints.*;

import java.time.LocalDate;


public record AuthDto(
        @NotBlank String nome,

        @NotBlank @Email String email,

        @NotNull @Past LocalDate dataNascimento,

        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, com letras maiúsculas, minúsculas e números."
        )
        @NotBlank String senha
) {}

