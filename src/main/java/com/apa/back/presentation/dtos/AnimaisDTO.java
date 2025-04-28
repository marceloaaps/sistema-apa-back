package com.apa.back.presentation.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AnimaisDTO {

    private Long id;
    private String nome;
    private Integer idade;
    private String raca;
    private Long idSaude;
    private String comportamento;
    private String historico;
    private LocalDate dataCadastro;
    private Boolean disponivelParaAdocao;

    public AnimaisDTO(Long id, String nome, Integer idade, String raca, Long idSaude,
                      String comportamento, String historico, LocalDate dataCadastro, Boolean disponivelParaAdocao) {
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
}
