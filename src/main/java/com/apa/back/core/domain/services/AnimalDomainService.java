package com.apa.back.core.domain.services;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.builder.AnimalBuilder;
import com.apa.back.core.domain.repositories.AnimalRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimalDomainService {

    private final AnimalRepository animalRepository;

    public AnimalDomainService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal CreateAnimal(Animal animal){

        AnimalBuilder animalBuilder = new AnimalBuilder();
        animalBuilder.sexo(animal.getSexo());
        animalBuilder.nome(animal.getNome());
        animalBuilder.cor(animal.getCor());
        animalBuilder.id(animal.getId());
        animalBuilder.especie(animal.getEspecie());
        animalBuilder.disponivelParaAdocao(animal.getDisponivelParaAdocao());
        animalBuilder.comportamento(animal.getComportamento());
        animalBuilder.id(animal.getId());
        animalBuilder.historicoSaude(animal.getHistoricoSaude());

        animalBuilder.build();

        return animalRepository.save(animal);
    }


}
