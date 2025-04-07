package com.apa.back.presentation.controllers;

import com.apa.back.core.use_cases.auth.UserUseCase;
import com.apa.back.presentation.dtos.AuthDto;
import com.apa.back.presentation.dtos.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
public class LoginController {

    private final UserUseCase userUseCase;


    public LoginController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthDto authDto) {
        userUseCase.register(authDto);
        return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        var jwt = userUseCase.login(loginRequest);

        return ResponseEntity.status(200).body(jwt);

    }

}