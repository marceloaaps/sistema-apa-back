package com.apa.back.presentation.v1.dtos.animal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para histórico de saúde do animal")
public class HistoricoSaudeDto {

    @Schema(description = "ID do evento de saúde", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "O ID do animal é obrigatório")
    @Schema(description = "ID do animal", example = "123", required = true)
    private Long idAnimal;

    @NotBlank(message = "O tipo de evento é obrigatório")
    @Size(max = 50, message = "O tipo de evento deve ter no máximo 50 caracteres")
    @Schema(
            description = "Tipo do evento de saúde",
            example = "Doença",
            required = true,
            allowableValues = {"Doença", "Deficiência", "Histórico", "Castração", "Consulta", "Cirurgia", "Exame", "Tratamento"}
    )
    private String tipoEvento;

    @Size(max = 5000, message = "A descrição deve ter no máximo 5000 caracteres")
    @Schema(
            description = "Descrição detalhada do evento",
            example = "Animal apresentou sintomas de gripe. Foi medicado com antibióticos.",
            maxLength = 5000
    )
    private String descricao;

    @Schema(description = "Indica se o animal foi castrado", example = "true", defaultValue = "false")
    private Boolean castrado;

    @Schema(
            description = "Data e hora do evento",
            example = "2024-01-15T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime dataEvento;
}
