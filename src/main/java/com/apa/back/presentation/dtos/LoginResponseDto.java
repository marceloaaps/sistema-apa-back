package com.apa.back.presentation.dtos;

public record LoginResponseDto(String token, Long expiresIn) {
}
