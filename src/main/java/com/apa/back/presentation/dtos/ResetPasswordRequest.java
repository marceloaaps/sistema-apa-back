package com.apa.back.presentation.dtos;

public record ResetPasswordRequest(String token, String newPassword) {}
