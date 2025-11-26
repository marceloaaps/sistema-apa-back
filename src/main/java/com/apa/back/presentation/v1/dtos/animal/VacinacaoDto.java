package com.apa.back.presentation.v1.dtos.animal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registro de vacinação do animal")
public class VacinacaoDto {

    @Schema(description = "ID da vacinação", example = "1")
    private Long id;

    @Schema(description = "ID do animal", example = "123")
    private Long idAnimal;

    @Schema(description = "Nome da vacina", example = "V10")
    private String nomeVacina;

    @Schema(description = "Data de aplicação da vacina", example = "2024-01-15")
    private LocalDate dataAplicacao;

    @Schema(description = "Dose aplicada", example = "1ª dose")
    private String dose;

    @Schema(description = "Data de validade da vacina", example = "2025-01-15")
    private LocalDate validade;

    @Schema(description = "Nome do veterinário responsável", example = "Dr. João Silva")
    private String veterinario;

    @Schema(description = "Observações sobre a vacinação", example = "Animal tolerou bem a vacina")
    private String observacoes;
}

