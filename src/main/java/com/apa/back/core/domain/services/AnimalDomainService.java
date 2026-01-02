package com.apa.back.core.domain.services;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.builder.AnimalBuilder;
import com.apa.back.core.domain.repositories.AnimalRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimalDomainService {

    private final AnimalRepository animalRepository;

    public AnimalDomainService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal CreateAnimal(Animal animal){

        return animalRepository.save(animal);
    }


}
