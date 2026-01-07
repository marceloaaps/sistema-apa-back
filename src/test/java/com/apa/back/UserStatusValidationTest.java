package com.apa.back;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.core.domain.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserStatusValidationTest {

    @Test
    @DisplayName("Deve criar usuário com status APPROVED")
    void shouldCreateUserWithApprovedStatus() {
        // Arrange & Act
        User user = new User()
                .setId(1L)
                .setNome("Test User")
                .setEmail("test@test.com")
                .setDataNascimento(LocalDate.of(1990, 1, 1))
                .setSenha("hashedPassword")
                .setUserStatus(UserStatus.APPROVED)
                .setUserRole(UserRole.USER);

        // Assert
        assertEquals(UserStatus.APPROVED, user.getUserStatus());
        assertTrue(isUserApproved(user));
    }

    @Test
    @DisplayName("Deve criar usuário com status PENDING")
    void shouldCreateUserWithPendingStatus() {
        // Arrange & Act
        User user = new User()
                .setId(2L)
                .setNome("Pending User")
                .setEmail("pending@test.com")
                .setDataNascimento(LocalDate.of(1991, 1, 1))
                .setSenha("hashedPassword")
                .setUserStatus(UserStatus.PENDING)
                .setUserRole(UserRole.USER);

        // Assert
        assertEquals(UserStatus.PENDING, user.getUserStatus());
        assertFalse(isUserApproved(user));
    }

    @Test
    @DisplayName("Deve criar usuário com status REJECTED")
    void shouldCreateUserWithRejectedStatus() {
        // Arrange & Act
        User user = new User()
                .setId(3L)
                .setNome("Rejected User")
                .setEmail("rejected@test.com")
                .setDataNascimento(LocalDate.of(1992, 1, 1))
                .setSenha("hashedPassword")
                .setUserStatus(UserStatus.REJECTED)
                .setUserRole(UserRole.USER);

        // Assert
        assertEquals(UserStatus.REJECTED, user.getUserStatus());
        assertFalse(isUserApproved(user));
    }

    @Test
    @DisplayName("Deve validar que apenas usuário APPROVED pode acessar o sistema")
    void shouldValidateOnlyApprovedUserCanAccessSystem() {
        // Arrange
        User approvedUser = createUserWithStatus(UserStatus.APPROVED);
        User pendingUser = createUserWithStatus(UserStatus.PENDING);
        User rejectedUser = createUserWithStatus(UserStatus.REJECTED);

        // Assert
        assertTrue(isUserApproved(approvedUser), "Usuário APPROVED deve ter acesso");
        assertFalse(isUserApproved(pendingUser), "Usuário PENDING não deve ter acesso");
        assertFalse(isUserApproved(rejectedUser), "Usuário REJECTED não deve ter acesso");
    }

    @Test
    @DisplayName("Deve alterar status de usuário de PENDING para APPROVED")
    void shouldChangeUserStatusFromPendingToApproved() {
        // Arrange
        User user = createUserWithStatus(UserStatus.PENDING);
        assertEquals(UserStatus.PENDING, user.getUserStatus());

        // Act
        user.setUserStatus(UserStatus.APPROVED);

        // Assert
        assertEquals(UserStatus.APPROVED, user.getUserStatus());
        assertTrue(isUserApproved(user));
    }

    @Test
    @DisplayName("Deve alterar status de usuário de PENDING para REJECTED")
    void shouldChangeUserStatusFromPendingToRejected() {
        // Arrange
        User user = createUserWithStatus(UserStatus.PENDING);
        assertEquals(UserStatus.PENDING, user.getUserStatus());

        // Act
        user.setUserStatus(UserStatus.REJECTED);

        // Assert
        assertEquals(UserStatus.REJECTED, user.getUserStatus());
        assertFalse(isUserApproved(user));
    }

    @Test
    @DisplayName("Deve validar enum UserStatus com todos os valores possíveis")
    void shouldValidateAllUserStatusValues() {
        // Assert
        assertEquals(3, UserStatus.values().length);
        assertNotNull(UserStatus.valueOf("APPROVED"));
        assertNotNull(UserStatus.valueOf("PENDING"));
        assertNotNull(UserStatus.valueOf("REJECTED"));
    }

    @Test
    @DisplayName("Deve retornar valor correto do enum UserStatus")
    void shouldReturnCorrectValueFromUserStatus() {
        // Assert
        assertEquals("APPROVED", UserStatus.APPROVED.getValue());
        assertEquals("PENDING", UserStatus.PENDING.getValue());
        assertEquals("REJECTED", UserStatus.REJECTED.getValue());
    }

    // Helper methods
    private User createUserWithStatus(UserStatus status) {
        return new User()
                .setId(1L)
                .setNome("Test User")
                .setEmail("test@test.com")
                .setDataNascimento(LocalDate.of(1990, 1, 1))
                .setSenha("hashedPassword")
                .setUserStatus(status)
                .setUserRole(UserRole.USER);
    }

    private boolean isUserApproved(User user) {
        return user.getUserStatus() == UserStatus.APPROVED;
    }
}

