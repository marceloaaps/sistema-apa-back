package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.config.ConfigUseCase;
import com.apa.back.presentation.v1.dtos.user.UsuarioDto;
import com.apa.back.presentation.v1.dtos.user.UsuarioRoleDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configs/v1")
public class AuthController {

    private final ConfigUseCase configUseCase;

    public AuthController(ConfigUseCase configUseCase) {
        this.configUseCase = configUseCase;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateRole(@PathVariable Long id, @RequestBody UsuarioRoleDto usuarioRoleDto) {

    UsuarioDto user = configUseCase.updateUsuario(id, usuarioRoleDto);


    return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(user);
    }
}
