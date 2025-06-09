package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.use_cases.auth.AuthUseCase;
import com.apa.back.core.use_cases.auth.PasswordResetUseCase;
import com.apa.back.presentation.dtos.auth.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(
            summary = "Registrar novo usuário",
            description = "Registra um novo usuário no sistema e gera um token de autenticação.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                            content = @Content(mediaType = "text/plain")),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para registro",
                            content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Parameter(description = "Dados para registro do usuário", required = true)
            @Valid @RequestBody RegisterDto registerDto) {
        authUseCase.registerAndGenerateToken(registerDto);
        return ResponseEntity.status(201).body("Usuário criado com sucesso.");
    }

    @Operation(
            summary = "Login do usuário",
            description = "Autentica o usuário e retorna um token JWT para uso nas requisições subsequentes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                            content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Parameter(description = "Credenciais do usuário para login", required = true)
            @RequestBody LoginRequestDto loginRequestDto) {
        var response = authUseCase.login(loginRequestDto);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(
            summary = "Solicitar redefinição de senha",
            description = "Envia um token para o e-mail informado para iniciar o processo de redefinição de senha.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token enviado para o e-mail",
                            content = @Content(mediaType = "text/plain")),
                    @ApiResponse(responseCode = "404", description = "E-mail não encontrado",
                            content = @Content)
            }
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Parameter(description = "E-mail do usuário para enviar o token de redefinição", required = true)
            @RequestBody ForgotPasswordRequestDto request) {
        resetUseCase.sendResetToken(request.email());
        return ResponseEntity.status(200).body("Token enviado para o email informado.");
    }

    @Operation(
            summary = "Redefinir senha do usuário",
            description = "Redefine a senha do usuário usando o token enviado por e-mail e a nova senha.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso",
                            content = @Content(mediaType = "text/plain")),
                    @ApiResponse(responseCode = "400", description = "Token inválido ou senha inválida",
                            content = @Content)
            }
    )
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Dados do token e nova senha para redefinição", required = true)
            @RequestBody ResetPasswordRequestDto request) {
        resetUseCase.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.status(200).body("Senha redefinida com sucesso.");
    }

}
