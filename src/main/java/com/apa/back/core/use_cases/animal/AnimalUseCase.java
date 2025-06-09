package com.apa.back.core.use_cases.animal;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.presentation.dtos.animal.AnimalDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AnimalUseCase {

    private final AnimalRepository animalRepository;

    public AnimalUseCase(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal createAnimal(AnimalDto animalDTO) {
        Animal animal = new Animal(
                null,
                animalDTO.getNome(),
                animalDTO.getIdade(),
                animalDTO.getRaca(),
                animalDTO.getIdSaude(),
                animalDTO.getComportamento(),
                animalDTO.getHistorico(),
                animalDTO.getDataCadastro(),
                animalDTO.getDisponivelParaAdocao()
        );
        return animalRepository.save(animal);
    }

    public Animal updateAnimal(Long id, AnimalDto animalDTO) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (!existingAnimal.isPresent()) {
            return null;
        }

        Animal animal = existingAnimal.get();
        animal.setNome(animalDTO.getNome());
        animal.setIdade(animalDTO.getIdade());
        animal.setRaca(animalDTO.getRaca());
        animal.setIdSaude(animalDTO.getIdSaude());
        animal.setComportamento(animalDTO.getComportamento());
        animal.setHistorico(animalDTO.getHistorico());
        animal.setDataCadastro(animalDTO.getDataCadastro());
        animal.setDisponivelParaAdocao(animalDTO.getDisponivelParaAdocao());

        return animalRepository.save(animal);
    }

    public boolean deleteAnimal(Long id) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (!existingAnimal.isPresent()) {
            return false;
        }

        Animal animal = existingAnimal.get();
        animal.setDisponivelParaAdocao(false);
        animal.setDeletadoEm(LocalDate.now());

        animalRepository.save(animal);
        return true;
    }

    public Animal restoreAnimal(Long id) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (!existingAnimal.isPresent()) {
            return null;
        }

        Animal animal = existingAnimal.get();
        animal.setDeletadoEm(null);
        animal.setDeletadoPor(null);
        animal.setDisponivelParaAdocao(true);

        return animalRepository.save(animal);
    }

    public AnimalDto getAnimalById(Long id) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (!existingAnimal.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado");
        }

        Animal animal = existingAnimal.get();
        return new AnimalDto(
                animal.getId(),
                animal.getNome(),
                animal.getIdade(),
                animal.getRaca(),
                animal.getIdSaude(),
                animal.getComportamento(),
                animal.getHistorico(),
                animal.getDataCadastro(),
                animal.getDisponivelParaAdocao()
        );
    }

    public Page<AnimalDto> getAnimaisDisponiveis(Pageable pageable) {
        return animalRepository.findAllByDisponivelParaAdocaoTrue(pageable)
                .map(animal -> new AnimalDto(
                        animal.getId(),
                        animal.getNome(),
                        animal.getIdade(),
                        animal.getRaca(),
                        animal.getIdSaude(),
                        animal.getComportamento(),
                        animal.getHistorico(),
                        animal.getDataCadastro(),
                        animal.getDisponivelParaAdocao()
                ));
    }


}
