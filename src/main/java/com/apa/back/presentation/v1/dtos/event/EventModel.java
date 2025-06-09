package com.apa.back.presentation.v1.dtos.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class EventModel extends RepresentationModel<EventModel> {
    private final EventDto dto;

    public EventModel(EventDto dto) {
        this.dto = dto;
    }
}