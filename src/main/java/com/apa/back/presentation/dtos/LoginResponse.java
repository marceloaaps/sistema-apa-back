package com.apa.back.presentation.dtos;

public record LoginResponse(String token, Long expiresIn) {
}
