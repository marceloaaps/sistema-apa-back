package com.apa.back;

import com.apa.back.core.domain.services.interfaces.AuthRateLimitService;
import com.apa.back.core.exceptions.DomainConflictException;
import com.apa.back.core.use_cases.auth.AuthUseCase;
import com.apa.back.core.use_cases.auth.PasswordResetUseCase;
import com.apa.back.presentation.v1.controllers.LoginController;
import com.apa.back.presentation.v1.dtos.auth.*;
import com.apa.back.presentation.v1.dtos.auth.login.LoginRequestDto;
import com.apa.back.presentation.v1.dtos.auth.login.LoginResponseDto;
import com.apa.back.presentation.v1.dtos.auth.password.ForgotPasswordRequestDto;
import com.apa.back.presentation.v1.dtos.auth.password.ResetPasswordRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private AuthUseCase authUseCase;

    @Mock
    private PasswordResetUseCase resetUseCase;

    @InjectMocks
    private LoginController loginController;

    @Mock
    private AuthRateLimitService authRateLimitService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("Deve chamar use case e retornar 201 ao registrar usuário")
    void register_deveChamarUseCaseERetornar201() {
        RegisterDto registerDto = new RegisterDto("Neymar Jr", "njr@psg.com", LocalDate.of(1992, 2, 1), "Senhateste@123");
        when(authUseCase.registerAndGenerateToken(any(RegisterDto.class))).thenReturn(new LoginResponseDto("token-fake", 3600L));

        ResponseEntity<LoginResponseDto> response = loginController.register(registerDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token-fake", response.getBody().token());
        verify(authUseCase).registerAndGenerateToken(registerDto);
    }

    @Test
    @DisplayName("Deve lançar exceção para senha inválida (muito longa)")
    void register_deveLancarExcecaoParaSenhaInvalida() {
        RegisterDto registerDto = new RegisterDto("Neymar Jr", "neyney@gmail.com", LocalDate.of(1992,
                2, 1), "SenhaMuitoLongaQueExcedeSetentaEDoisCaracteres12345678asdasdasdasd2354324342342421@@#34@353453445543434322439045435342423423243243");

       doThrow(new DomainConflictException("A senha não pode ter mais de 72 caracteres")).when(authUseCase).registerAndGenerateToken(any(RegisterDto.class));

        DomainConflictException exception = assertThrows(DomainConflictException.class, () ->
                loginController.register(registerDto));

        assertEquals("A senha não pode ter mais de 72 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar token ao fazer login com credenciais válidas")
    void login_deveRetornarToken() {
        LoginRequestDto loginRequest = new LoginRequestDto("usuario", "senha123");
        LoginResponseDto loginResponse = new LoginResponseDto("token-jwt-exemplo", 3500L);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getHeader("x-forwarded-for")).thenReturn("192.168.1.1");
        doNothing().when(authRateLimitService).validateAttempt(anyString());
        when(authUseCase.login(loginRequest)).thenReturn(loginResponse);

        ResponseEntity<LoginResponseDto> response = loginController.login(loginRequest, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token-jwt-exemplo", response.getBody().token());
        verify(authUseCase).login(loginRequest);
    }

    @Test
    @DisplayName("Deve chamar use case e retornar 200 ao solicitar reset de senha")
    void forgotPassword_deveChamarUseCaseERetornar200() {
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto("email@exemplo.com");
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getHeader("x-forwarded-for")).thenReturn("192.168.1.1");
        doNothing().when(authRateLimitService).validateAttempt(anyString());
        doNothing().when(resetUseCase).sendResetToken(request.email());

        ResponseEntity<String> response = loginController.forgotPassword(request, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Token enviado para o email informado.", response.getBody());
        verify(resetUseCase).sendResetToken(request.email());
    }

    @Test
    @DisplayName("Deve resetar senha e retornar 200")
    void shouldResetPasswordAndReturn200() {
        ResetPasswordRequestDto request =
                new ResetPasswordRequestDto("token123", "novaSenha123");

        ResponseEntity<String> response = loginController.resetPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Senha redefinida com sucesso.", response.getBody());

        verify(resetUseCase).resetPassword(request.token(), request.newPassword());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar registrar com email já existente")
    void register_deveLancarExcecaoParaEmailDuplicado() {
        RegisterDto registerDto = new RegisterDto("João Silva", "duplicado@test.com",
                LocalDate.of(1990, 1, 1), "Senha@123");

        doThrow(new DomainConflictException("Esse email já existe."))
                .when(authUseCase).registerAndGenerateToken(any(RegisterDto.class));

        DomainConflictException exception = assertThrows(DomainConflictException.class, () ->
                loginController.register(registerDto));

        assertEquals("Esse email já existe.", exception.getMessage());
        verify(authUseCase).registerAndGenerateToken(registerDto);
    }

    @Test
    @DisplayName("Deve lançar exceção ao fazer login com credenciais inválidas")
    void login_deveLancarExcecaoParaCredenciaisInvalidas() {
        LoginRequestDto loginRequest = new LoginRequestDto("usuario@test.com", "senhaErrada");
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getHeader("x-forwarded-for")).thenReturn("192.168.1.1");
        doNothing().when(authRateLimitService).validateAttempt(anyString());
        doThrow(new DomainConflictException("Credencial inválida."))
                .when(authUseCase).login(any(LoginRequestDto.class));

        DomainConflictException exception = assertThrows(DomainConflictException.class, () ->
                loginController.login(loginRequest, httpRequest));

        assertEquals("Credencial inválida.", exception.getMessage());
        verify(authUseCase).login(loginRequest);
    }

}
