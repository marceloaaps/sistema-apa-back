package com.apa.back.presentation.controllers;

import com.apa.back.presentation.dtos.AnimalDTO;
import com.apa.back.core.domain.repositories.AnimalRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalRepository animalRepository;

    public AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @GetMapping
    public List<AnimalDTO> getAnimaisDisponiveis() {
        return animalRepository.findAllAnimalsForAdoption();
    }
}
