package com.apa.back.core.domain.entities.builder;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.HistoricoSaude;
import com.apa.back.core.domain.entities.Vacinacao;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnimalBuilder {

    private Long id;
    private String nome;
    private Integer idade;
    private String raca;
    private String rgAnimal;
    private String especie = "Desconhecida";
    private String sexo;
    private String cor;
    private String comportamento;
    private String historico;
    private LocalDate dataCadastro;
    private Boolean disponivelParaAdocao;
    private LocalDate deletadoEm;
    private Integer deletadoPor;
    private List<HistoricoSaude> historicoSaude = new ArrayList<>();
    private List<Vacinacao> vacinacoes = new ArrayList<>();

    public AnimalBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public AnimalBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public AnimalBuilder idade(Integer idade) {
        this.idade = idade;
        return this;
    }

    public AnimalBuilder raca(String raca) {
        this.raca = raca;
        return this;
    }

    public AnimalBuilder rgAnimal(String rgAnimal) {
        this.rgAnimal = rgAnimal;
        return this;
    }

    public AnimalBuilder especie(String especie) {
        this.especie = especie;
        return this;
    }

    public AnimalBuilder sexo(String sexo) {
        this.sexo = sexo;
        return this;
    }

    public AnimalBuilder cor(String cor) {
        this.cor = cor;
        return this;
    }

    public AnimalBuilder comportamento(String comportamento) {
        this.comportamento = comportamento;
        return this;
    }

    public AnimalBuilder historico(String historico) {
        this.historico = historico;
        return this;
    }

    public AnimalBuilder dataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public AnimalBuilder disponivelParaAdocao(Boolean disponivel) {
        this.disponivelParaAdocao = disponivel;
        return this;
    }

    public AnimalBuilder deletadoEm(LocalDate deletadoEm) {
        this.deletadoEm = deletadoEm;
        return this;
    }

    public AnimalBuilder deletadoPor(Integer deletadoPor) {
        this.deletadoPor = deletadoPor;
        return this;
    }

    public AnimalBuilder historicoSaude(List<HistoricoSaude> historicoSaude) {
        this.historicoSaude = historicoSaude;
        return this;
    }

    public AnimalBuilder vacinacoes(List<Vacinacao> vacinacoes) {
        this.vacinacoes = vacinacoes;
        return this;
    }

    public Animal build() {
        return new Animal(
                id,
                nome,
                idade,
                raca,
                rgAnimal,
                especie,
                sexo,
                cor,
                comportamento,
                historico,
                dataCadastro,
                disponivelParaAdocao,
                deletadoEm,
                deletadoPor,
                historicoSaude,
                vacinacoes
        );
    }
}
