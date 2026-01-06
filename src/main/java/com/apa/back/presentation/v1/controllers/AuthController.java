package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.usuario.UsuarioUseCase;
import com.apa.back.presentation.v1.dtos.user.UsuarioDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AuthController {

    private final UsuarioUseCase usuarioUseCase;

    public AuthController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> getAllPending() {
        List<UsuarioDto> users = usuarioUseCase.getAllPending();
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<Void> approveUser(@PathVariable Long id) {
        usuarioUseCase.approveUser(id);
        return ResponseEntity.noContent().build();
    }
}
