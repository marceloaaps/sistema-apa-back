package com.apa.back.presentation.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


//Falta resto dos parametros/atributos
public record AuthDto(@NotBlank @Email String email, @NotBlank String password) {
}
