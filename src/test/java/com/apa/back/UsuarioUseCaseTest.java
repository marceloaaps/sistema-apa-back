package com.apa.back;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.core.domain.enums.UserStatus;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.core.use_cases.usuario.UsuarioUseCase;
import com.apa.back.presentation.v1.dtos.user.UsuarioDto;
import com.apa.back.presentation.v1.dtos.user.UsuarioRoleDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    private User approvedUser;
    private User pendingUser;
    private User rejectedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        approvedUser = new User()
                .setId(1L)
                .setNome("Approved User")
                .setEmail("approved@test.com")
                .setDataNascimento(LocalDate.of(1990, 1, 1))
                .setSenha("hashedPassword")
                .setUserStatus(UserStatus.APPROVED)
                .setUserRole(UserRole.USER);

        pendingUser = new User()
                .setId(2L)
                .setNome("Pending User")
                .setEmail("pending@test.com")
                .setDataNascimento(LocalDate.of(1991, 1, 1))
                .setSenha("hashedPassword")
                .setUserStatus(UserStatus.PENDING)
                .setUserRole(UserRole.USER);

        rejectedUser = new User()
                .setId(3L)
                .setNome("Rejected User")
                .setEmail("rejected@test.com")
                .setDataNascimento(LocalDate.of(1992, 1, 1))
                .setSenha("hashedPassword")
                .setUserStatus(UserStatus.REJECTED)
                .setUserRole(UserRole.USER);
    }

    @Test
    @DisplayName("Deve retornar usuário por email")
    void shouldReturnUserByEmail() {
        // Arrange
        when(userRepository.findByEmail("approved@test.com")).thenReturn(Optional.of(approvedUser));

        // Act
        Optional<UsuarioDto> result = usuarioUseCase.getUsuario("approved@test.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Approved User", result.get().nome());
        assertEquals("approved@test.com", result.get().email());
        verify(userRepository, times(1)).findByEmail("approved@test.com");
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando usuário não existe")
    void shouldReturnEmptyOptionalWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act
        Optional<UsuarioDto> result = usuarioUseCase.getUsuario("nonexistent@test.com");

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("nonexistent@test.com");
    }

    @Test
    @DisplayName("Deve retornar todos os usuários pendentes")
    void shouldReturnAllPendingUsers() {
        // Arrange
        User pendingUser2 = new User()
                .setId(4L)
                .setNome("Another Pending")
                .setEmail("pending2@test.com")
                .setDataNascimento(LocalDate.of(1993, 1, 1))
                .setUserStatus(UserStatus.PENDING);

        List<User> pendingUsers = Arrays.asList(pendingUser, pendingUser2);
        when(userRepository.findAllByUserStatus(UserStatus.PENDING)).thenReturn(pendingUsers);

        // Act
        List<UsuarioDto> result = usuarioUseCase.getAllPending();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Pending User", result.get(0).nome());
        assertEquals("Another Pending", result.get(1).nome());
        verify(userRepository, times(1)).findAllByUserStatus(UserStatus.PENDING);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há usuários pendentes")
    void shouldReturnEmptyListWhenNoPendingUsers() {
        // Arrange
        when(userRepository.findAllByUserStatus(UserStatus.PENDING)).thenReturn(Collections.emptyList());

        // Act
        List<UsuarioDto> result = usuarioUseCase.getAllPending();

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAllByUserStatus(UserStatus.PENDING);
    }

    @Test
    @DisplayName("Deve aprovar usuário com sucesso")
    void shouldApproveUserSuccessfully() {
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.of(pendingUser));
        when(userRepository.save(any(User.class))).thenReturn(pendingUser);

        // Act
        usuarioUseCase.approveUser(2L);

        // Assert
        assertEquals(UserStatus.APPROVED, pendingUser.getUserStatus());
        verify(userRepository, times(1)).findById(2L);
        verify(userRepository, times(1)).save(pendingUser);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aprovar usuário inexistente")
    void shouldThrowExceptionWhenApprovingNonExistentUser() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> usuarioUseCase.approveUser(999L));
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar role do usuário")
    void shouldUpdateUserRole() {
        // Arrange
        UsuarioRoleDto roleDto = new UsuarioRoleDto("ADMIN");
        when(userRepository.findById(1L)).thenReturn(Optional.of(approvedUser));
        doNothing().when(userRepository).updateUserRoleById(any(UserRole.class), anyLong());

        // Act
        UsuarioDto result = usuarioUseCase.updateUsuario(1L, roleDto);

        // Assert
        assertNotNull(result);
        assertEquals("Approved User", result.nome());
        assertEquals(UserRole.ADMIN, approvedUser.getUserRole());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).updateUserRoleById(UserRole.ADMIN, 1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar role de usuário inexistente")
    void shouldThrowExceptionWhenUpdatingNonExistentUserRole() {
        // Arrange
        UsuarioRoleDto roleDto = new UsuarioRoleDto("ADMIN");
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
            usuarioUseCase.updateUsuario(999L, roleDto));
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).updateUserRoleById(any(UserRole.class), anyLong());
    }

    @Test
    @DisplayName("Deve filtrar apenas usuários com status PENDING")
    void shouldFilterOnlyPendingUsers() {
        // Arrange
        List<User> allUsers = Arrays.asList(approvedUser, pendingUser, rejectedUser);
        when(userRepository.findAllByUserStatus(UserStatus.PENDING))
                .thenReturn(allUsers.stream()
                        .filter(user -> user.getUserStatus() == UserStatus.PENDING)
                        .toList());

        // Act
        List<UsuarioDto> result = usuarioUseCase.getAllPending();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Pending User", result.get(0).nome());
        verify(userRepository, times(1)).findAllByUserStatus(UserStatus.PENDING);
    }

    @Test
    @DisplayName("Deve converter User para UsuarioDto corretamente")
    void shouldConvertUserToUsuarioDtoCorrectly() {
        // Arrange
        when(userRepository.findByEmail("approved@test.com")).thenReturn(Optional.of(approvedUser));

        // Act
        Optional<UsuarioDto> result = usuarioUseCase.getUsuario("approved@test.com");

        // Assert
        assertTrue(result.isPresent());
        UsuarioDto dto = result.get();
        assertEquals(approvedUser.getNome(), dto.nome());
        assertEquals(approvedUser.getEmail(), dto.email());
        assertEquals(approvedUser.getDataNascimento().toString(), dto.dataNascimento());
    }
}

