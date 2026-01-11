package com.apa.back.core.use_cases.animal;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.Vacinacao;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.core.domain.repositories.VacinacaoRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.presentation.v1.dtos.animal.VacinacaoDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacinacaoUseCase {

    private static final Logger logger = LogManager.getLogger(VacinacaoUseCase.class);

    private final VacinacaoRepository vacinacaoRepository;
    private final AnimalRepository animalRepository;

    public VacinacaoUseCase(VacinacaoRepository vacinacaoRepository, AnimalRepository animalRepository) {
        this.vacinacaoRepository = vacinacaoRepository;
        this.animalRepository = animalRepository;
    }


    @Transactional
    public VacinacaoDto createVacinacao(VacinacaoDto dto) {
        logger.info("Criando vacinação para animal ID: {}, Vacina: {}", dto.getIdAnimal(), dto.getNomeVacina());

        try {
            Animal animal = animalRepository.findById(dto.getIdAnimal())
                    .orElseThrow(() -> {
                        logger.warn("Animal não encontrado para vacinação - ID: {}", dto.getIdAnimal());
                        return new DomainNotFoundException("Animal não encontrado: ID " + dto.getIdAnimal());
                    });

            Vacinacao vacinacao = new Vacinacao(
                    animal,
                    dto.getNomeVacina(),
                    dto.getDataAplicacao(),
                    dto.getDose(),
                    dto.getValidade(),
                    dto.getVeterinario(),
                    dto.getObservacoes()
            );

            Vacinacao saved = vacinacaoRepository.save(vacinacao);
            logger.info("Vacinação criada com sucesso - ID: {}, Animal: {}, Vacina: {}",
                    saved.getId(), animal.getNome(), saved.getNomeVacina());

            return mapToDto(saved);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao criar vacinação: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar vacinação para animal ID: {}", dto.getIdAnimal(), e);
            throw new RuntimeException("Erro ao criar vacinação", e);
        }
    }

    @Transactional(readOnly = true)
    public List<VacinacaoDto> getVacinacoesByAnimal(Long animalId) {
        logger.debug("Buscando vacinações do animal ID: {}", animalId);

        try {
            if (!animalRepository.existsById(animalId)) {
                logger.warn("Animal não encontrado ao buscar vacinações - ID: {}", animalId);
                throw new DomainNotFoundException("Animal não encontrado: ID " + animalId);
            }

            List<VacinacaoDto> vacinacoes = vacinacaoRepository.findByAnimalIdOrderByDataAplicacaoDesc(animalId)
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            logger.debug("Encontradas {} vacinações para animal ID: {}", vacinacoes.size(), animalId);
            return vacinacoes;

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao buscar vacinações: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar vacinações do animal ID: {}", animalId, e);
            throw new RuntimeException("Erro ao buscar vacinações", e);
        }
    }

    @Transactional(readOnly = true)
    public VacinacaoDto getVacinacaoById(Long id) {
        logger.debug("Buscando vacinação ID: {}", id);

        try {
            Vacinacao vacinacao = vacinacaoRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Vacinação não encontrada - ID: {}", id);
                        return new DomainNotFoundException("Vacinação não encontrada: ID " + id);
                    });

            return mapToDto(vacinacao);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao buscar vacinação: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar vacinação ID: {}", id, e);
            throw new RuntimeException("Erro ao buscar vacinação", e);
        }
    }

    @Transactional(readOnly = true)
    public List<VacinacaoDto> getVacinacoesVencidas() {
        logger.info("Buscando vacinações vencidas");

        try {
            List<VacinacaoDto> vencidas = vacinacaoRepository.findByValidadeBefore(LocalDate.now())
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            logger.info("Encontradas {} vacinações vencidas", vencidas.size());
            return vencidas;

        } catch (Exception e) {
            logger.error("Erro ao buscar vacinações vencidas", e);
            throw new RuntimeException("Erro ao buscar vacinações vencidas", e);
        }
    }

    @Transactional
    public VacinacaoDto updateVacinacao(Long id, VacinacaoDto dto) {
        logger.info("Atualizando vacinação ID: {}", id);

        try {
            Vacinacao vacinacao = vacinacaoRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Vacinação não encontrada para atualização - ID: {}", id);
                        return new DomainNotFoundException("Vacinação não encontrada: ID " + id);
                    });

            vacinacao.setNomeVacina(dto.getNomeVacina());
            vacinacao.setDataAplicacao(dto.getDataAplicacao());
            vacinacao.setDose(dto.getDose());
            vacinacao.setValidade(dto.getValidade());
            vacinacao.setVeterinario(dto.getVeterinario());
            vacinacao.setObservacoes(dto.getObservacoes());

            Vacinacao updated = vacinacaoRepository.save(vacinacao);
            logger.info("Vacinação atualizada com sucesso - ID: {}, Vacina: {}", id, updated.getNomeVacina());

            return mapToDto(updated);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao atualizar vacinação: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar vacinação ID: {}", id, e);
            throw new RuntimeException("Erro ao atualizar vacinação", e);
        }
    }

    @Transactional
    public void deleteVacinacao(Long id) {
        logger.info("Deletando vacinação ID: {}", id);

        try {
            if (!vacinacaoRepository.existsById(id)) {
                logger.warn("Vacinação não encontrada para exclusão - ID: {}", id);
                throw new DomainNotFoundException("Vacinação não encontrada: ID " + id);
            }

            vacinacaoRepository.deleteById(id);
            logger.info("Vacinação deletada com sucesso - ID: {}", id);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao deletar vacinação: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar vacinação ID: {}", id, e);
            throw new RuntimeException("Erro ao deletar vacinação", e);
        }
    }

    private VacinacaoDto mapToDto(Vacinacao vacinacao) {
        return VacinacaoDto.builder()
                .id(vacinacao.getId())
                .idAnimal(vacinacao.getAnimal().getId())
                .nomeVacina(vacinacao.getNomeVacina())
                .dataAplicacao(vacinacao.getDataAplicacao())
                .dose(vacinacao.getDose())
                .validade(vacinacao.getValidade())
                .veterinario(vacinacao.getVeterinario())
                .observacoes(vacinacao.getObservacoes())
                .build();
    }
}

