package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.use_cases.auth.AuthUseCase;
import com.apa.back.core.use_cases.auth.PasswordResetUseCase;
import com.apa.back.presentation.dtos.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
public class LoginController {

    private final AuthUseCase authUseCase;
    private final PasswordResetUseCase resetUseCase;

    public LoginController(AuthUseCase authUseCase, PasswordResetUseCase resetUseCase) {
        this.authUseCase = authUseCase;
        this.resetUseCase = resetUseCase;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthDto authDto) {
        authUseCase.registerAndGenerateToken(authDto);
        return ResponseEntity.status(201).body("Usuário criado com sucesso.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        var response = authUseCase.login(loginRequestDto);

        return ResponseEntity.status(200).body(response);

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDto request) {
        resetUseCase.sendResetToken(request.email());
        return ResponseEntity.status(200).body("Token enviado para o email informado.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDto request) {
        resetUseCase.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.status(200).body("Senha redefinida com sucesso.");
    }

}