package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.use_cases.usuario.UsuarioUseCase;
import com.apa.back.presentation.dtos.UsuarioDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios/v1")
public class UsuarioController {

    private final UsuarioUseCase userUseCase;

    public UsuarioController(UsuarioUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping
    public ResponseEntity<UsuarioDto> getUsuario(@RequestParam String email) {
        Optional<UsuarioDto> user = userUseCase.getUsuario(email);

        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }




}
