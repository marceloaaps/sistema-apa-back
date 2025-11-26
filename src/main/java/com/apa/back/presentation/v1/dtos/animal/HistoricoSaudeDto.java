package com.apa.back.presentation.v1.dtos.animal;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "ID do evento de saúde", example = "1")
    private Long id;

    @Schema(description = "ID do animal", example = "123")
    private Long idAnimal;

    @Schema(description = "Tipo do evento", example = "Doença", allowableValues = {"Doença", "Deficiência", "Histórico", "Castração", "Consulta", "Cirurgia"})
    private String tipoEvento;

    @Schema(description = "Descrição detalhada do evento", example = "Animal apresentou sintomas de gripe")
    private String descricao;

    @Schema(description = "Indica se o animal foi castrado", example = "true")
    private Boolean castrado;

    @Schema(description = "Data e hora do evento", example = "2024-01-15T10:30:00")
    private LocalDateTime dataEvento;
}

