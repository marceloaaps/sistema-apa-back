package com.apa.back.presentation.v1.dtos.animal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Schema(description = "RG único do animal", example = "RG-2024-001")
    private String rgAnimal;

    @Schema(description = "Espécie do animal", example = "Cachorro")
    private String especie;

    @Schema(description = "Sexo do animal", example = "M", allowableValues = {"M", "F"})
    private String sexo;

    @Schema(description = "Cor do animal", example = "Marrom")
    private String cor;

    @Schema(description = "Descrição do comportamento do animal", example = "Calmo e amigável")
    private String comportamento;

    @Schema(description = "Histórico do animal", example = "Encontrado na rua, saudável")
    private String historico;

    @Schema(description = "Data de cadastro do animal", example = "2023-04-01")
    private LocalDate dataCadastro;

    @Schema(description = "Indica se o animal está disponível para adoção", example = "true")
    private Boolean disponivelParaAdocao;

    @Schema(description = "Data em que o animal foi deletado (soft delete)", example = "2023-05-15")
    private LocalDate deletadoEm;

    @Schema(description = "ID do usuário que deletou o animal", example = "42")
    private Integer deletadoPor;

    @Schema(description = "Lista de eventos do histórico de saúde do animal")
    private List<HistoricoSaudeDto> historicoSaude = new ArrayList<>();

    @Schema(description = "Lista de vacinações do animal")
    private List<VacinacaoDto> vacinacoes = new ArrayList<>();

    public AnimalDto(Long id, String nome, Integer idade, String raca, String rgAnimal,
                     String especie, String sexo, String cor, String comportamento,
                     String historico, LocalDate dataCadastro, Boolean disponivelParaAdocao,
                     LocalDate deletadoEm, Integer deletadoPor, List<HistoricoSaudeDto> historicoSaude,
                     List<VacinacaoDto> vacinacoes) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.rgAnimal = rgAnimal;
        this.especie = especie;
        this.sexo = sexo;
        this.cor = cor;
        this.comportamento = comportamento;
        this.historico = historico;
        this.dataCadastro = dataCadastro;
        this.disponivelParaAdocao = disponivelParaAdocao;
        this.deletadoEm = deletadoEm;
        this.deletadoPor = deletadoPor;
        this.historicoSaude = historicoSaude;
        this.vacinacoes = vacinacoes;
    }
}
