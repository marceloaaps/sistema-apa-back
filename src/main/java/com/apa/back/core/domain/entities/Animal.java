package com.apa.back.core.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_animal")
    private Long id;

    private String nome;
    private Integer idade;
    private String raca;
    private Long idSaude;
    private String comportamento;
    private String historico;
    private LocalDate dataCadastro;
    private Boolean disponivelParaAdocao;

    @Column(name = "deletado_em")
    private LocalDate deletadoEm;

    @Column(name = "deletado_por")
    private Integer deletadoPor;


    public Animal() {
    }

    public Animal(Long id, String nome, Integer idade, String raca, Long idSaude, String comportamento, String historico, LocalDate dataCadastro, boolean disponivelParaAdocao) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.idSaude = idSaude;
        this.comportamento = comportamento;
        this.historico = historico;
        this.dataCadastro = dataCadastro;
        this.disponivelParaAdocao = disponivelParaAdocao;
    }

    public void softDelete(Integer deletadoPor) {
        this.disponivelParaAdocao = false;
        this.deletadoEm = LocalDate.now();
        this.deletadoPor = deletadoPor;
    }
}
