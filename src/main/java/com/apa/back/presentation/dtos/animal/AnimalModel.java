package com.apa.back.presentation.dtos.animal;

import org.springframework.hateoas.RepresentationModel;

public class AnimalModel extends RepresentationModel<AnimalModel> {

    private final AnimalDto animalDto;

    public AnimalModel(AnimalDto animalDto) {
        this.animalDto = animalDto;
    }

    public AnimalDto getAnimalDto() {
        return animalDto;
    }
}
