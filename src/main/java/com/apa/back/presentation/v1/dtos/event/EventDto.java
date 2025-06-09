package com.apa.back.presentation.v1.dtos.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

@Schema(name = "EventDto", description = "DTO para criação/atualização de eventos (feirinhas)")
public record EventDto(

        @Schema(description = "ID do evento (feirinha)", example = "1", nullable = true)
        Long idFeirinha,

        @NotNull(message = "A data de início do evento é obrigatória")
        @Schema(description = "Data de início do evento", example = "2025-06-10T08:00:00Z", required = true)
        Date dataInicioFeira,

        @NotNull(message = "A data de fim do evento é obrigatória")
        @Schema(description = "Data de fim do evento", example = "2025-06-10T17:00:00Z", required = true)
        Date dataFimFeira,

        @NotNull(message = "A localização é obrigatória")
        @NotEmpty(message = "A localização não pode ser vazia")
        @Schema(description = "Localização do evento", example = "Praça Central", required = true)
        String localizacao,

        @NotNull(message = "A lista de IDs de voluntários é obrigatória")
        @Size(min = 1, message = "Deve haver pelo menos um voluntário")
        @Schema(description = "Lista de IDs dos voluntários", example = "[1, 2, 3]", required = true)
        List<Long> idsVoluntarios,

        @NotNull(message = "A lista de IDs de animais é obrigatória")
        @Size(min = 1, message = "Deve haver pelo menos um animal")
        @Schema(description = "Lista de IDs dos animais", example = "[10, 20, 30]", required = true)
        List<Long> idsAnimais

) {}
