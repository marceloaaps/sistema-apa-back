package com.apa.back.presentation.v1.assemblers;

import com.apa.back.presentation.v1.dtos.event.ReturnEventDto;
import com.apa.back.presentation.v1.dtos.event.ReturnEventModel;
import com.apa.back.presentation.v1.controllers.EventController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class ReturnEventModelAssembler {

    public static ReturnEventModel toModel(ReturnEventDto dto) {
        ReturnEventModel model = new ReturnEventModel(dto);

        Long id = dto.idFeirinha();

        model.add(linkTo(methodOn(EventController.class).getEventById(id)).withSelfRel());
        model.add(linkTo(methodOn(EventController.class).updateEvent(id, null)).withRel("update"));
        model.add(linkTo(methodOn(EventController.class).deleteEvent(id)).withRel("delete"));

        return model;
    }
}

