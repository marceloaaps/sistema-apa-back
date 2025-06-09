package com.apa.back.presentation.v1.dtos.event;

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
