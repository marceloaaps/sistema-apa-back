package com.apa.back.core.domain.entities;

import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.core.domain.enums.UserStatus;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, name = "senha")
    private String senha;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", columnDefinition = "user_role_enum", insertable = false)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", columnDefinition = "user_status_enum")
    private UserStatus userStatus;

    @Column(name = "deletado_em")
    private LocalDate deletadoEm;

    @Column(name = "deletado_por")
    private Long deletadoPor;


    public User() {
    }

    public User(String nome, LocalDate dataNascimento, LocalDate deletadoEm, Long deletadoPor, String email,
                Long id, String senha, UserRole userRole, UserStatus userStatus) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.deletadoEm = deletadoEm;
        this.deletadoPor = deletadoPor;
        this.email = email;
        this.id = id;
        this.senha = senha;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    public LocalDate getDeletadoEm() {
        return deletadoEm;
    }

    public User setDeletadoEm(LocalDate deletadoEm) {
        this.deletadoEm = deletadoEm;
        return this;
    }

    public Long getDeletadoPor() {
        return deletadoPor;
    }

    public User setDeletadoPor(Long deletadoPor) {
        this.deletadoPor = deletadoPor;
        return this;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public User setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public User setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getSenha() {
        return senha;
    }

    public User setSenha(String senha) {
        this.senha = senha;
        return this;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public User setUserRole(UserRole userRole) {
        this.userRole = userRole;
        return this;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public User setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
        return this;
    }
}
