package com.apa.back.presentation.controllers;

import com.apa.back.core.use_cases.user.UserUseCase;
import com.apa.back.presentation.dtos.AuthDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private UserUseCase userUseCase;



    @PostMapping("/register/v1")
    public ResponseEntity<String> register(@Valid @RequestBody AuthDto authDto) {
        userUseCase.register(authDto);
        return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!");
    }

}