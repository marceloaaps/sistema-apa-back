package com.apa.back.core.use_cases.animal;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.presentation.v1.dtos.animal.AnimalDto;
import com.apa.back.presentation.v1.dtos.animal.HistoricoSaudeDto;
import com.apa.back.presentation.v1.dtos.animal.VacinacaoDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AnimalUseCase {

    private static final Logger logger = LogManager.getLogger(AnimalUseCase.class);

    private final AnimalRepository animalRepository;

    public AnimalUseCase(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public AnimalDto createAnimal(AnimalDto animalDTO) {
        logger.info("Iniciando criação de animal: {}", animalDTO.getNome());

        try {
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
            logger.info("Animal criado com sucesso - ID: {}, Nome: {}", animalRes.getId(), animalRes.getNome());

            return mapToDto(animalRes);

        } catch (Exception e) {
            logger.error("Erro ao criar animal: {}", animalDTO.getNome(), e);
            throw new RuntimeException("Erro ao criar animal", e);
        }
    }

    public AnimalDto updateAnimal(Long id, AnimalDto animalDTO) {
        logger.info("Iniciando atualização do animal ID: {}", id);

        try {
            Optional<Animal> existingAnimal = animalRepository.findById(id);

            if (existingAnimal.isEmpty()) {
                logger.warn("Animal não encontrado para atualização - ID: {}", id);
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
            logger.info("Animal atualizado com sucesso - ID: {}, Nome: {}", id, animal.getNome());

            return mapToDto(animal);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao atualizar animal: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar animal ID: {}", id, e);
            throw new RuntimeException("Erro ao atualizar animal", e);
        }
    }

    public void deleteAnimal(Long id) {
        logger.info("Iniciando soft delete do animal ID: {}", id);

        try {
            Optional<Animal> existingAnimal = animalRepository.findById(id);

            if (existingAnimal.isEmpty()) {
                logger.warn("Animal não encontrado para exclusão - ID: {}", id);
                throw new DomainNotFoundException("Animal não encontrado: ID " + id);
            }

            Animal animal = existingAnimal.get();
            animal.setDisponivelParaAdocao(false);
            animal.setDeletadoEm(LocalDate.now());

            animalRepository.save(animal);
            logger.info("Animal marcado como deletado com sucesso - ID: {}, Nome: {}", id, animal.getNome());

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao deletar animal: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar animal ID: {}", id, e);
            throw new RuntimeException("Erro ao deletar animal", e);
        }
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
        logger.info("Buscando animal por ID: {}", id);

        try {
            Optional<Animal> existingAnimal = animalRepository.findById(id);

            if (existingAnimal.isEmpty()) {
                logger.warn("Animal não encontrado - ID: {}", id);
                throw new DomainNotFoundException("Animal não encontrado");
            }

            Animal animal = existingAnimal.get();
            logger.debug("Animal encontrado - ID: {}, Nome: {}", id, animal.getNome());

            return mapToDto(animal);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao buscar animal: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar animal ID: {}", id, e);
            throw new RuntimeException("Erro ao buscar animal", e);
        }
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
