package com.apa.back.presentation.dtos;

import java.util.Date;
import java.util.List;

public record ReturnEventDto(
        Long idResponsavel,
        Long idFeirinha,
        Date dataInicioFeira,
        Date dataFimFeira,
        String localizacao,
        List<UsuarioWithIdDto> usuarios,
        List<AnimalDto> animais
) {}
