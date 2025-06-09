package com.apa.back.presentation.v1.assemblers;

import com.apa.back.presentation.dtos.event.EventDto;
import com.apa.back.presentation.dtos.event.EventModel;
import com.apa.back.presentation.v1.controllers.EventController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class EventModelAssembler {

    public static EventModel toModel(EventDto dto) {
        EventModel model = new EventModel(dto);

        Long id = dto.idFeirinha();

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).getEventById(id)).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).updateEvent(id, null)).withRel("update"));
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EventController.class).deleteEvent(id)).withRel("delete"));

        return model;
    }
}
