package com.apa.back.core.domain.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private User user;

    private Instant expirationDate;

    public String getToken() {
        return token;
    }

    public PasswordResetToken setToken(String token) {
        this.token = token;
        return this;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public PasswordResetToken setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

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
}
