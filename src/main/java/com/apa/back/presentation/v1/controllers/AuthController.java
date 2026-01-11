package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.usuario.UsuarioUseCase;
import com.apa.back.presentation.v1.dtos.user.UsuarioDto;
import com.apa.back.presentation.v1.dtos.user.UsuarioRoleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Lista todos os usuários pendentes de aprovação",
    description = "Retorna uma lista de todos os usuários que estão aguardando aprovação para acesso ao sistema.",
    responses = {
        @ApiResponse(
                responseCode = "200",
                description = "Lista de usuarios pendentes a aprovação.",
                content = @Content(mediaType = "text/plain",
                schema = @Schema(implementation =  UsuarioDto.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Usuário não encontrado",
                content = @Content
        ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro ao aprovar usuário",
                    content = @Content
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> getAllPending() {
        List<UsuarioDto> users = usuarioUseCase.getAllPending();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Aprova um usuário pendente",
            description = "Aprova o usuário com o ID especificado, concedendo acesso ao sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Usuário aprovado com sucesso",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<Void> approveUser(@PathVariable Long id) {
        usuarioUseCase.approveUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza um usuário pendente",
            description = "Aprova o usuário com o ID especificado, concedendo acesso ao sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioDto.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateRole(@PathVariable Long id, @RequestBody UsuarioRoleDto usuarioRoleDto) {

        UsuarioDto user = usuarioUseCase.updateUsuario(id, usuarioRoleDto);
        return ResponseEntity.ok(user);
    }
}
