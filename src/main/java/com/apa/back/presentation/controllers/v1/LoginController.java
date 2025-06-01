package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.use_cases.auth.AuthUseCase;
import com.apa.back.presentation.dtos.AuthDto;
import com.apa.back.presentation.dtos.LoginRequest;
import com.apa.back.presentation.dtos.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
public class LoginController {

    private final AuthUseCase authUseCase;

    public LoginController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthDto authDto) {
        authUseCase.registerAndGenerateToken(authDto);
        return ResponseEntity.status(201).body("Usuário criado com sucesso.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var response = authUseCase.login(loginRequest);

        return ResponseEntity.status(200).body(response);

    }

}