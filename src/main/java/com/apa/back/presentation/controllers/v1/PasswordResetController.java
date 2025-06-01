package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.use_cases.auth.PasswordResetUseCase;
import com.apa.back.presentation.dtos.ForgotPasswordRequest;
import com.apa.back.presentation.dtos.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    private final PasswordResetUseCase resetUseCase;

    public PasswordResetController(PasswordResetUseCase resetUseCase) {
        this.resetUseCase = resetUseCase;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        resetUseCase.sendResetToken(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        resetUseCase.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok().build();
    }
}

