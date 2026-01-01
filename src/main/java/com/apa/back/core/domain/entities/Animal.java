package com.apa.back.core.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_animal")
    private Long id;

    @Column(nullable = false)
    private String nome;

    private Integer idade;

    private String raca;

    @Column(name = "rg_animal", unique = true, length = 50)
    private String rgAnimal;

    @Column(nullable = false, length = 100)
    private String especie = "Desconhecida";

    @Column(length = 1)
    private String sexo;

    @Column(length = 100)
    private String cor;

    private String comportamento;

    private String historico;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "disponivel_para_adocao")
    private Boolean disponivelParaAdocao;

    @Column(name = "deletado_em")
    private LocalDate deletadoEm;

    @Column(name = "deletado_por")
    private Integer deletadoPor;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoSaude> historicoSaude = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacinacao> vacinacoes = new ArrayList<>();

    public Animal() {
    }

    public Animal(Long id, String nome, Integer idade, String raca, String rgAnimal, String especie, String sexo,
                  String cor, String comportamento, String historico, LocalDate dataCadastro, Boolean disponivelParaAdocao,
                  LocalDate deletadoEm, Integer deletadoPor, List<HistoricoSaude> historicoSaude,
                  List<Vacinacao> vacinacoes) {
        this.deletadoEm = deletadoEm;
        this.comportamento = comportamento;
        this.cor = cor;
        this.dataCadastro = dataCadastro;
        this.deletadoPor = deletadoPor;
        this.disponivelParaAdocao = disponivelParaAdocao;
        this.especie = especie;
        this.historico = historico;
        this.historicoSaude = historicoSaude;
        this.id = id;
        this.idade = idade;
        this.nome = nome;
        this.raca = raca;
        this.rgAnimal = rgAnimal;
        this.sexo = sexo;
        this.vacinacoes = vacinacoes;
    }

    public void softDelete(Integer deletadoPor) {
        this.disponivelParaAdocao = false;
        this.deletadoEm = LocalDate.now();
        this.deletadoPor = deletadoPor;
    }

    public void addHistoricoSaude(HistoricoSaude evento) {
        historicoSaude.add(evento);
        evento.setAnimal(this);
    }

    public void removeHistoricoSaude(HistoricoSaude evento) {
        historicoSaude.remove(evento);
        evento.setAnimal(null);
    }

    public void addVacinacao(Vacinacao vacinacao) {
        vacinacoes.add(vacinacao);
        vacinacao.setAnimal(this);
    }

    public void removeVacinacao(Vacinacao vacinacao) {
        vacinacoes.remove(vacinacao);
        vacinacao.setAnimal(null);
    }

    public void AnimalBuilder(Long id, String nome, Integer idade, String raca, String rgAnimal, String especie,
                         String sexo, String cor, String comportamento, String historico,
                         LocalDate dataCadastro, boolean disponivelParaAdocao) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.rgAnimal = rgAnimal;
        this.especie = especie != null ? especie : "Desconhecida";
        this.sexo = sexo;
        this.cor = cor;
        this.comportamento = comportamento;
        this.historico = historico;
        this.dataCadastro = dataCadastro;
        this.disponivelParaAdocao = disponivelParaAdocao;
    }

}
