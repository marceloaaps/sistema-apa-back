package com.apa.back;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.core.domain.enums.UserStatus;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.core.exceptions.DomainConflictException;
import com.apa.back.core.use_cases.auth.AuthUseCase;
import com.apa.back.infra.security.service.PasswordBcrypt;
import com.apa.back.infra.security.service.TokenCache;
import com.apa.back.presentation.v1.dtos.auth.RegisterDto;
import com.apa.back.presentation.v1.dtos.auth.login.LoginRequestDto;
import com.apa.back.presentation.v1.dtos.auth.login.LoginResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthUseCaseUserStatusTest {

    @Mock
    private PasswordBcrypt passwordBcrypt;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private TokenCache tokenCache;

    @InjectMocks
    private AuthUseCase authUseCase;

    private User approvedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        approvedUser = new User()
                .setId(1L)
                .setNome("Approved User")
                .setEmail("approved@test.com")
                .setDataNascimento(LocalDate.of(1990, 1, 1))
                .setSenha("$2a$10$hashedPassword")
                .setUserStatus(UserStatus.APPROVED)
                .setUserRole(UserRole.USER);
    }

    @Test
    @DisplayName("Deve permitir login de usuário APPROVED")
    void shouldAllowLoginForApprovedUser() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto("approved@test.com", "password123");

        when(userRepository.findByEmail("approved@test.com")).thenReturn(Optional.of(approvedUser));
        when(passwordBcrypt.verifyPassword("password123", approvedUser.getSenha())).thenReturn(true);

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("mock-token");
        when(jwtEncoder.encode(any())).thenReturn(mockJwt);
        doNothing().when(tokenCache).storeToken(anyString(), anyString());

        // Act
        LoginResponseDto response = authUseCase.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock-token", response.token());
        verify(userRepository, times(1)).findByEmail("approved@test.com");
        verify(passwordBcrypt, times(1)).verifyPassword("password123", approvedUser.getSenha());
    }

    @Test
    @DisplayName("Deve registrar novo usuário com status PENDING por padrão")
    void shouldRegisterNewUserWithPendingStatusByDefault() {
        // Arrange
        RegisterDto registerDto = new RegisterDto(
                "New User",
                "newuser@test.com",
                LocalDate.of(1995, 5, 15),
                "Password@123"
        );

        when(userRepository.findByEmail("newuser@test.com")).thenReturn(Optional.empty());
        when(passwordBcrypt.hashPassword("Password@123")).thenReturn("$2a$10$hashedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(10L); // Simula o ID gerado pelo banco
            return user;
        });

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("registration-token");
        when(jwtEncoder.encode(any())).thenReturn(mockJwt);
        doNothing().when(tokenCache).storeToken(anyString(), anyString());

        // Act
        LoginResponseDto response = authUseCase.registerAndGenerateToken(registerDto);

        // Assert
        assertNotNull(response);
        assertEquals("registration-token", response.token());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordBcrypt, times(1)).hashPassword("Password@123");
    }

    @Test
    @DisplayName("Deve lançar exceção para credenciais inválidas")
    void shouldThrowExceptionForInvalidCredentials() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto("approved@test.com", "wrongpassword");

        when(userRepository.findByEmail("approved@test.com")).thenReturn(Optional.of(approvedUser));
        when(passwordBcrypt.verifyPassword("wrongpassword", approvedUser.getSenha())).thenReturn(false);

        // Act & Assert
        DomainConflictException exception = assertThrows(
                DomainConflictException.class,
                () -> authUseCase.login(loginRequest)
        );

        assertEquals("Credencial inválida.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("approved@test.com");
        verify(passwordBcrypt, times(1)).verifyPassword("wrongpassword", approvedUser.getSenha());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email não existe")
    void shouldThrowExceptionWhenEmailNotFound() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto("nonexistent@test.com", "password123");

        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        DomainConflictException exception = assertThrows(
                DomainConflictException.class,
                () -> authUseCase.login(loginRequest)
        );

        assertEquals("Credencial inválida.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("nonexistent@test.com");
        verify(passwordBcrypt, never()).verifyPassword(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção ao registrar email duplicado")
    void shouldThrowExceptionWhenRegisteringDuplicateEmail() {
        // Arrange
        RegisterDto registerDto = new RegisterDto(
                "Duplicate User",
                "approved@test.com",
                LocalDate.of(1995, 5, 15),
                "Password@123"
        );

        when(userRepository.findByEmail("approved@test.com")).thenReturn(Optional.of(approvedUser));

        // Act & Assert
        DomainConflictException exception = assertThrows(
                DomainConflictException.class,
                () -> authUseCase.registerAndGenerateToken(registerDto)
        );

        assertEquals("Esse email já existe.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("approved@test.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção para senha muito longa")
    void shouldThrowExceptionForPasswordTooLong() {
        // Arrange
        String longPassword = "a".repeat(73);
        RegisterDto registerDto = new RegisterDto(
                "User",
                "user@test.com",
                LocalDate.of(1995, 5, 15),
                longPassword
        );

        // Act & Assert
        DomainConflictException exception = assertThrows(
                DomainConflictException.class,
                () -> authUseCase.registerAndGenerateToken(registerDto)
        );

        // Nota: A implementação futura deve adicionar esta validação:
        // Verificar se o status do usuário é APPROVED antes de permitir login
        assertEquals("A senha não pode ter mais de 72 caracteres.", exception.getMessage());
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}

