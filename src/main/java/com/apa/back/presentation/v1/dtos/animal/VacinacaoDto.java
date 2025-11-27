package com.apa.back.presentation.v1.dtos.animal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
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

    @Schema(description = "ID da vacinação", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "O ID do animal é obrigatório")
    @Schema(description = "ID do animal", example = "123", required = true)
    private Long idAnimal;

    @NotBlank(message = "O nome da vacina é obrigatório")
    @Size(max = 100, message = "O nome da vacina deve ter no máximo 100 caracteres")
    @Schema(
            description = "Nome da vacina aplicada",
            example = "V10",
            required = true,
            maxLength = 100
    )
    private String nomeVacina;

    @NotNull(message = "A data de aplicação é obrigatória")
    @PastOrPresent(message = "A data de aplicação não pode ser futura")
    @Schema(
            description = "Data de aplicação da vacina",
            example = "2024-01-15",
            required = true
    )
    private LocalDate dataAplicacao;

    @Size(max = 50, message = "A dose deve ter no máximo 50 caracteres")
    @Schema(
            description = "Dose aplicada",
            example = "1ª dose",
            maxLength = 50
    )
    private String dose;

    @Schema(
            description = "Data de validade da vacina",
            example = "2025-01-15"
    )
    private LocalDate validade;

    @Size(max = 255, message = "O nome do veterinário deve ter no máximo 255 caracteres")
    @Schema(
            description = "Nome do veterinário responsável pela aplicação",
            example = "Dr. João Silva",
            maxLength = 255
    )
    private String veterinario;

    @Size(max = 5000, message = "As observações devem ter no máximo 5000 caracteres")
    @Schema(
            description = "Observações sobre a vacinação",
            example = "Animal tolerou bem a vacina. Reforço recomendado em 1 ano.",
            maxLength = 5000
    )
    private String observacoes;
}
