package com.apa.back.core.use_cases.animal;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.presentation.v1.dtos.animal.AnimalDto;
import com.apa.back.presentation.v1.dtos.animal.HistoricoSaudeDto;
import com.apa.back.presentation.v1.dtos.animal.VacinacaoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AnimalUseCase {

    private final AnimalRepository animalRepository;

    public AnimalUseCase(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public AnimalDto createAnimal(AnimalDto animalDTO) {
        Animal animal = new Animal();
        animal.AnimalBuilder(null,
                animalDTO.getNome(),
                animalDTO.getIdade(),
                animalDTO.getRaca(),
                animalDTO.getRgAnimal(),
                animalDTO.getEspecie(),
                animalDTO.getSexo(),
                animalDTO.getCor(),
                animalDTO.getComportamento(),
                animalDTO.getHistorico(),
                animalDTO.getDataCadastro(),
                animalDTO.getDisponivelParaAdocao() != null ? animalDTO.getDisponivelParaAdocao() : true);
        Animal animalRes = animalRepository.save(animal);

        return mapToDto(animalRes);
    }

    public AnimalDto updateAnimal(Long id, AnimalDto animalDTO) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (existingAnimal.isEmpty()) {
            throw new DomainNotFoundException("Animal não encontrado: ID " + id);
        }

        Animal animal = new Animal();
        animal.AnimalBuilder(
                animalDTO.getId(),
                animalDTO.getNome(),
                animalDTO.getIdade(),
                animalDTO.getRaca(),
                animalDTO.getRgAnimal(),
                animalDTO.getEspecie(),
                animalDTO.getSexo(),
                animalDTO.getCor(),
                animalDTO.getComportamento(),
                animalDTO.getHistorico(),
                animalDTO.getDataCadastro(),
                animalDTO.getDisponivelParaAdocao());


        animalRepository.save(animal);

        return mapToDto(animal);
    }

    public void deleteAnimal(Long id) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (existingAnimal.isEmpty()) {
            throw new DomainNotFoundException("Animal não encontrado: ID " + id);
        }

        Animal animal = existingAnimal.get();
        animal.setDisponivelParaAdocao(false);
        animal.setDeletadoEm(LocalDate.now());

        animalRepository.save(animal);
    }

    public AnimalDto restoreAnimal(Long id) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (existingAnimal.isEmpty()) {
            throw new DomainNotFoundException("Animal não encontrado: ID " + id);
        }

        Animal animal = existingAnimal.get();
        animal.setDeletadoEm(null);
        animal.setDeletadoPor(null);
        animal.setDisponivelParaAdocao(true);

        animalRepository.save(animal);

        return mapToDto(animal);
    }

    public AnimalDto getAnimalById(Long id) {
        Optional<Animal> existingAnimal = animalRepository.findById(id);

        if (existingAnimal.isEmpty()) {
            throw new DomainNotFoundException("Animal não encontrado");
        }

        Animal animal = existingAnimal.get();
        return mapToDto(animal);
    }

    public Page<AnimalDto> getAnimaisDisponiveis(Pageable pageable) {
        return animalRepository.findAllByDisponivelParaAdocaoTrue(pageable)
                .map(this::mapToDto);
    }

    private AnimalDto mapToDto(Animal animal) {

        var historicoSaudeDto = animal.getHistoricoSaude().stream()
                .map(historico -> new HistoricoSaudeDto(
                        historico.getId(),
                        historico.getAnimal().getId(),
                        historico.getTipoEvento(),
                        historico.getDescricao(),
                        historico.getCastrado(),
                        historico.getDataEvento()
                ))
                .toList();

        var vacinacaoDto = animal.getVacinacoes().stream()
                .map(vacinacao -> new VacinacaoDto(
                        vacinacao.getId(),
                        vacinacao.getAnimal().getId(),
                        vacinacao.getNomeVacina(),
                        vacinacao.getDataAplicacao(),
                        vacinacao.getDose(),
                        vacinacao.getValidade(),
                        vacinacao.getVeterinario(),
                        vacinacao.getObservacoes()
                )).toList();


        return new AnimalDto(
                animal.getId(),
                animal.getNome(),
                animal.getIdade(),
                animal.getRaca(),
                animal.getRgAnimal(),
                animal.getEspecie(),
                animal.getSexo(),
                animal.getCor(),
                animal.getComportamento(),
                animal.getHistorico(),
                animal.getDataCadastro(),
                animal.getDisponivelParaAdocao(),
                animal.getDeletadoEm(),
                animal.getDeletadoPor(),
                historicoSaudeDto,
                vacinacaoDto
        );
    }
}
