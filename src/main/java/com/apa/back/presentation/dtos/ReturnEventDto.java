package com.apa.back.presentation.dtos;

import com.apa.back.core.domain.entities.EventAnimal;

import java.util.Date;
import java.util.List;

public record ReturnEventDto(
        Long idResponsavel,
        Date dataInicioFeira,
        Date dataFimFeira,
        String localizacao,
        List<EventAnimal> idsVoluntarios,
        List<com.apa.back.core.domain.entities.EventWorker> idsAnimais
) {}
