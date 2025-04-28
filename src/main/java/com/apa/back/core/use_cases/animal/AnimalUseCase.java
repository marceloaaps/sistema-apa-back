package com.apa.back.core.use_cases.animal;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.presentation.dtos.AnimalDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalUseCase {

    private final AnimalRepository animalRepository;

    public AnimalUseCase(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal createAnimal(AnimalDTO animalDTO) {
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

    public Animal updateAnimal(Long id, AnimalDTO animalDTO) {
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

    public boolean deleteAnimal(Long id, Integer deletadoPor) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (!existingAnimal.isPresent()) {
            return false;
        }

        Animal animal = existingAnimal.get();
        animal.setDisponivelParaAdocao(false);
        animal.setDeletadoEm(LocalDate.now());
        animal.setDeletadoPor(deletadoPor);

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

    public AnimalDTO getAnimalById(Long id) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (!existingAnimal.isPresent()) {
            return null;
        }

        Animal animal = existingAnimal.get();
        return new AnimalDTO(
                animal.getId(),
                animal.getNome(),
                animal.getIdade(),
                animal.getRaca(),
                animal.getIdSaude(),
                animal.getComportamento(),
                animal.getHistorico(),
                animal.getDataCadastro(),
                animal.isDisponivelParaAdocao()
        );
    }

    public List<AnimalDTO> getAnimaisDisponiveis() {
        List<Animal> animalDisponiveis = animalRepository.findAllByDisponivelParaAdocaoTrue();
        return animalDisponiveis.stream()
                .map(animal -> new AnimalDTO(
                        animal.getId(),
                        animal.getNome(),
                        animal.getIdade(),
                        animal.getRaca(),
                        animal.getIdSaude(),
                        animal.getComportamento(),
                        animal.getHistorico(),
                        animal.getDataCadastro(),
                        animal.isDisponivelParaAdocao()
                ))
                .collect(Collectors.toList());
    }
}
