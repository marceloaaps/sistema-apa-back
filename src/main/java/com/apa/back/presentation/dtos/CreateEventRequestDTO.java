package com.apa.back.presentation.dtos;

import java.util.Date;
import java.util.List;

public record CreateEventRequestDTO(
        Long idResponsavel,
        Date dataInicio,
        Date dataFim,
        String localizacao,
        List<Long> idsVoluntarios,
        List<Long> idsAnimais
) {}
