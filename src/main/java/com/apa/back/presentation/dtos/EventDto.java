package com.apa.back.presentation.dtos;

import java.util.Date;
import java.util.List;

public record EventDto(
        Long idFeirinha,
        Date dataInicioFeira,
        Date dataFimFeira,
        String localizacao,
        List<Long> idsVoluntarios,
        List<Long> idsAnimais
) {}
