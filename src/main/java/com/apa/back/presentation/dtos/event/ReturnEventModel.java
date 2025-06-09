package com.apa.back.presentation.dtos.event;

import com.apa.back.presentation.dtos.event.ReturnEventDto;
import org.springframework.hateoas.RepresentationModel;

public class ReturnEventModel extends RepresentationModel<ReturnEventModel> {
    private final ReturnEventDto dto;

    public ReturnEventModel(ReturnEventDto dto) {
        this.dto = dto;
    }

    public ReturnEventDto getDto() {
        return dto;
    }
}
