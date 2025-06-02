package com.apa.back.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResetPasswordRequestDto(String token, @JsonProperty(value = "new_password") String newPassword) {}
