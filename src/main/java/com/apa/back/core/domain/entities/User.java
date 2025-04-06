package com.apa.back.core.domain.entities;

import com.apa.back.core.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    private UserRole role;


}
