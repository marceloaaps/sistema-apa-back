package com.apa.back.presentation.v1.assemblers;


import com.apa.back.presentation.v1.controllers.AnimalController;
import com.apa.back.presentation.v1.dtos.animal.AnimalDto;
import com.apa.back.presentation.v1.dtos.animal.AnimalModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class AnimalModelAssembler {

    public static AnimalModel toModel(AnimalDto dto) {
        AnimalModel model = new AnimalModel(dto);

        model.add(linkTo(methodOn(AnimalController.class).getAnimalById(dto.getId())).withSelfRel());
        model.add(linkTo(AnimalController.class).withRel("animais"));
        model.add(linkTo(methodOn(AnimalController.class).updateAnimal(dto.getId(), dto)).withRel("update"));
        model.add(linkTo(methodOn(AnimalController.class).deleteAnimal(dto.getId())).withRel("delete"));
        model.add(linkTo(methodOn(AnimalController.class).restoreAnimal(dto.getId())).withRel("restore"));

        return model;
    }
}
