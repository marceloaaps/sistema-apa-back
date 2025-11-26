package com.apa.back.core.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vacinacoes")
public class Vacinacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacinacao")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_animal", nullable = false)
    private Animal animal;

    @Column(name = "nome_vacina", nullable = false, length = 100)
    private String nomeVacina;

    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @Column(name = "dose", length = 50)
    private String dose;

    @Column(name = "validade")
    private LocalDate validade;

    @Column(name = "veterinario", length = 255)
    private String veterinario;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    public Vacinacao(Animal animal, String nomeVacina, LocalDate dataAplicacao, String dose, LocalDate validade, String veterinario, String observacoes) {
        this.animal = animal;
        this.nomeVacina = nomeVacina;
        this.dataAplicacao = dataAplicacao;
        this.dose = dose;
        this.validade = validade;
        this.veterinario = veterinario;
        this.observacoes = observacoes;
    }
}

