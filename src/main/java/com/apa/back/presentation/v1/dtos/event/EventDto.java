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
        @Schema(description = "Data de início do evento", example = "2025-06-10T08:00:00Z")
        Date dataInicioFeira,

        @NotNull(message = "A data de fim do evento é obrigatória")
        @Schema(description = "Data de fim do evento", example = "2025-06-10T17:00:00Z")
        Date dataFimFeira,

        @NotNull(message = "A localização é obrigatória")
        @NotEmpty(message = "A localização não pode ser vazia")
        @Schema(description = "Localização do evento", example = "Praça Central")
        String localizacao,

        @NotNull(message = "A lista de IDs de voluntários é obrigatória")
        @Size(min = 1, message = "Deve haver pelo menos um voluntário")
        @Schema(description = "Lista de IDs dos voluntários", example = "[1, 2, 3]")
        List<Long> idsVoluntarios,

        @NotNull(message = "A lista de IDs de animais é obrigatória")
        @Size(min = 1, message = "Deve haver pelo menos um animal")
        @Schema(description = "Lista de IDs dos animais", example = "[10, 20, 30]")
        List<Long> idsAnimais,

        @NotNull (message = "Obrigatório ter pessoa responsável pelo evento")
        @Size(min = 1, max = 1, message = "Deve haver um responsável")
        @Schema(description = "ID do Usuário responsável pelo evento")
        Long idResponsavel

) {}
