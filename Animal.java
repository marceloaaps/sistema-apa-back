package com.apa.back.core.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String raca;
    private Integer idade;
    private boolean disponivelParaAdocao;

    public Animal() {
    }

    public Animal(Long id, String nome, String raca, Integer idade, boolean disponivelParaAdocao) {
        this.id = id;
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.disponivelParaAdocao = disponivelParaAdocao;
    }

    // Getters e Setters

}
