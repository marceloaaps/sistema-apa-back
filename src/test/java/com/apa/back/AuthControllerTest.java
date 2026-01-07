package com.apa.back;

import com.apa.back.core.domain.enums.UserStatus;
import com.apa.back.core.use_cases.usuario.UsuarioUseCase;
import com.apa.back.presentation.v1.controllers.AuthController;
import com.apa.back.presentation.v1.dtos.user.UsuarioDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UsuarioUseCase usuarioUseCase;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar lista de usuários pendentes com sucesso")
    void shouldReturnPendingUsersSuccessfully() {
        // Arrange
        UsuarioDto user1 = new UsuarioDto("João Silva", "joao@test.com", "1990-01-01");
        UsuarioDto user2 = new UsuarioDto("Maria Santos", "maria@test.com", "1992-05-15");
        List<UsuarioDto> expectedUsers = Arrays.asList(user1, user2);

        when(usuarioUseCase.getAllPending()).thenReturn(expectedUsers);

        // Act
        ResponseEntity<List<UsuarioDto>> response = authController.getAllPending();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("João Silva", response.getBody().get(0).nome());
        assertEquals("Maria Santos", response.getBody().get(1).nome());
        verify(usuarioUseCase, times(1)).getAllPending();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há usuários pendentes")
    void shouldReturnEmptyListWhenNoPendingUsers() {
        // Arrange
        when(usuarioUseCase.getAllPending()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<UsuarioDto>> response = authController.getAllPending();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(usuarioUseCase, times(1)).getAllPending();
    }

    @Test
    @DisplayName("Deve aprovar usuário com sucesso")
    void shouldApproveUserSuccessfully() {
        // Arrange
        Long userId = 1L;
        doNothing().when(usuarioUseCase).approveUser(userId);

        // Act
        ResponseEntity<Void> response = authController.approveUser(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioUseCase, times(1)).approveUser(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar usuário inexistente")
    void shouldThrowExceptionWhenApprovingNonExistentUser() {
        // Arrange
        Long userId = 999L;
        doThrow(new EntityNotFoundException("Usuário não encontrado"))
                .when(usuarioUseCase).approveUser(userId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> authController.approveUser(userId));
        verify(usuarioUseCase, times(1)).approveUser(userId);
    }

    @Test
    @DisplayName("Deve chamar o use case múltiplas vezes para diferentes aprovações")
    void shouldCallUseCaseMultipleTimesForDifferentApprovals() {
        // Arrange
        Long userId1 = 1L;
        Long userId2 = 2L;
        doNothing().when(usuarioUseCase).approveUser(anyLong());

        // Act
        authController.approveUser(userId1);
        authController.approveUser(userId2);

        // Assert
        verify(usuarioUseCase, times(1)).approveUser(userId1);
        verify(usuarioUseCase, times(1)).approveUser(userId2);
        verify(usuarioUseCase, times(2)).approveUser(anyLong());
    }
}

