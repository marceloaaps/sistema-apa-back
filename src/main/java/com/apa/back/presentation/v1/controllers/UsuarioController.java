package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.usuario.UsuarioUseCase;
import com.apa.back.presentation.dtos.user.UsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Busca um usuário pelo e-mail",
            description = "Retorna os dados do usuário caso exista um usuário com o e-mail informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioDto.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<UsuarioDto> getUsuario(@RequestParam String email) {
        Optional<UsuarioDto> user = userUseCase.getUsuario(email);

        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }




}
