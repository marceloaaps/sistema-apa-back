package com.apa.back.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public record EventDto(
        Long idResponsavel,
        Date dataInicioFeira,
        Date dataFimFeira,
        String localizacao,
        List<Long> idsVoluntarios,
        List<Long> idsAnimais
) {}
