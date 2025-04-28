package com.apa.back.presentation.dtos;

public class AnimalDTO {
    public Long id;
    public String nome;
    public String raca;
    public Integer idade;
    public Boolean disponivelParaAdocao;

    public AnimalDTO(Long id, String nome, String raca, Integer idade, Boolean disponivelParaAdocao) {
        this.id = id;
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.disponivelParaAdocao = disponivelParaAdocao;
    }

}
