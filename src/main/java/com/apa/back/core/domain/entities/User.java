package com.apa.back.core.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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


}
