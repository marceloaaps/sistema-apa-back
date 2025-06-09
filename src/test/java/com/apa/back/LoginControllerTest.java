package com.apa.back;

import com.apa.back.core.use_cases.auth.AuthUseCase;
import com.apa.back.core.use_cases.auth.PasswordResetUseCase;
import com.apa.back.presentation.v1.controllers.LoginController;
import com.apa.back.presentation.v1.dtos.auth.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private AuthUseCase authUseCase;

    @Mock
    private PasswordResetUseCase resetUseCase;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_deveChamarUseCaseERetornar201() {
        RegisterDto registerDto = new RegisterDto("Neymar Jr", "njr@psg.com", LocalDate.of(1992, 2, 1), "Senhateste@123");
        when(authUseCase.registerAndGenerateToken(any(RegisterDto.class))).thenReturn("token-fake");

        ResponseEntity<String> response = loginController.register(registerDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Usuário criado com sucesso.", response.getBody());
        verify(authUseCase).registerAndGenerateToken(registerDto);
    }


    @Test
    void login_deveRetornarToken() {
        LoginRequestDto loginRequest = new LoginRequestDto("usuario", "senha123");
        LoginResponseDto loginResponse = new LoginResponseDto("token-jwt-exemplo", 3500L);
        when(authUseCase.login(loginRequest)).thenReturn(loginResponse);

        ResponseEntity<LoginResponseDto> response = loginController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("token-jwt-exemplo", response.getBody().token());
        verify(authUseCase).login(loginRequest);
    }

    @Test
    void forgotPassword_deveChamarUseCaseERetornar200() {
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto("email@exemplo.com");
        doNothing().when(resetUseCase).sendResetToken(request.email());

        ResponseEntity<String> response = loginController.forgotPassword(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Token enviado para o email informado.", response.getBody());
        verify(resetUseCase).sendResetToken(request.email());
    }

    @Test
    void resetPassword_deveChamarUseCaseERetornar200() {
        ResetPasswordRequestDto request = new ResetPasswordRequestDto("token123", "novaSenha123");
        doNothing().when(resetUseCase).resetPassword(request.token(), request.newPassword());

        ResponseEntity<String> response = loginController.resetPassword(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Senha redefinida com sucesso.", response.getBody());
        verify(resetUseCase).resetPassword(request.token(), request.newPassword());
    }
}
