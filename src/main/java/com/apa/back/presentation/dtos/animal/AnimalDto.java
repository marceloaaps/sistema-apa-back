package com.apa.back.presentation.dtos.animal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class AnimalDto {

    @Schema(description = "ID único do animal", example = "123")
    private Long id;

    @Schema(description = "Nome do animal", example = "Rex")
    private String nome;

    @Schema(description = "Idade do animal em anos", example = "3")
    private Integer idade;

    @Schema(description = "Raça do animal", example = "Labrador")
    private String raca;

    @Schema(description = "ID do registro de saúde do animal", example = "456")
    private Long idSaude;

    @Schema(description = "Descrição do comportamento do animal", example = "Calmo e amigável")
    private String comportamento;

    @Schema(description = "Histórico do animal", example = "Encontrado na rua, saudável")
    private String historico;

    @Schema(description = "Data de cadastro do animal", example = "2023-04-01")
    private LocalDate dataCadastro;

    @Schema(description = "Indica se o animal está disponível para adoção", example = "true")
    private Boolean disponivelParaAdocao;



    public AnimalDto(Long id, String nome, Integer idade, String raca, Long idSaude,
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
