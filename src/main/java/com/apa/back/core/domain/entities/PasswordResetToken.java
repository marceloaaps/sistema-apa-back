package com.apa.back.core.domain.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_usuario")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    public Long getId() {
        return id;
    }

    public PasswordResetToken setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public PasswordResetToken setUser(User user) {
        this.user = user;
        return this;
    }

    public String getToken() {
        return token;
    }

    public PasswordResetToken setToken(String token) {
        this.token = token;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public PasswordResetToken setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public PasswordResetToken setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public boolean isUsed() {
        return used;
    }

    public PasswordResetToken setUsed(boolean used) {
        this.used = used;
        return this;
    }
}