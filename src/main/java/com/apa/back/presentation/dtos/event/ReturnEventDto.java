package com.apa.back.presentation.dtos.event;

import com.apa.back.presentation.dtos.animal.AnimalDto;
import com.apa.back.presentation.dtos.user.UsuarioWithIdDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

@Schema(name = "ReturnEventDto", description = "DTO para retorno de dados de um evento (feirinha)")
public record ReturnEventDto(

        @Schema(description = "ID do responsável pelo evento", example = "5", required = true)
        Long idResponsavel,

        @Schema(description = "ID do evento (feirinha)", example = "1", required = true)
        Long idFeirinha,

        @Schema(description = "Data de início do evento", example = "2025-06-10T08:00:00Z", required = true)
        Date dataInicioFeira,

        @Schema(description = "Data de fim do evento", example = "2025-06-10T17:00:00Z", required = true)
        Date dataFimFeira,

        @Schema(description = "Localização do evento", example = "Praça Central", required = true)
        String localizacao,

        @Schema(description = "Lista de usuários voluntários associados ao evento", required = true)
        List<UsuarioWithIdDto> usuarios,

        @Schema(description = "Lista de animais associados ao evento", required = true)
        List<AnimalDto> animais
) {}
