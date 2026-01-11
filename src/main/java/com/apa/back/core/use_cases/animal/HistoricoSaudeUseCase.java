package com.apa.back.core.use_cases.animal;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.HistoricoSaude;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.core.domain.repositories.HistoricoSaudeRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.presentation.v1.dtos.animal.HistoricoSaudeDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoSaudeUseCase {

    private static final Logger logger = LogManager.getLogger(HistoricoSaudeUseCase.class);

    private final HistoricoSaudeRepository historicoSaudeRepository;
    private final AnimalRepository animalRepository;

    public HistoricoSaudeUseCase(HistoricoSaudeRepository historicoSaudeRepository,
                                  AnimalRepository animalRepository) {
        this.historicoSaudeRepository = historicoSaudeRepository;
        this.animalRepository = animalRepository;
    }

    @Transactional
    public HistoricoSaudeDto createHistoricoSaude(HistoricoSaudeDto dto) {
        logger.info("Criando histórico de saúde para animal ID: {}, Tipo: {}",
                dto.getIdAnimal(), dto.getTipoEvento());

        try {
            Animal animal = animalRepository.findById(dto.getIdAnimal())
                    .orElseThrow(() -> {
                        logger.warn("Animal não encontrado para histórico de saúde - ID: {}", dto.getIdAnimal());
                        return new DomainNotFoundException("Animal não encontrado: ID " + dto.getIdAnimal());
                    });

            HistoricoSaude historicoSaude = new HistoricoSaude(
                    animal,
                    dto.getTipoEvento(),
                    dto.getDescricao(),
                    dto.getCastrado()
            );

            HistoricoSaude saved = historicoSaudeRepository.save(historicoSaude);
            logger.info("Histórico de saúde criado com sucesso - ID: {}, Animal: {}, Tipo: {}",
                    saved.getId(), animal.getNome(), saved.getTipoEvento());

            return mapToDto(saved);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao criar histórico de saúde: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar histórico de saúde para animal ID: {}", dto.getIdAnimal(), e);
            throw new RuntimeException("Erro ao criar histórico de saúde", e);
        }
    }

    @Transactional(readOnly = true)
    public List<HistoricoSaudeDto> getHistoricoByAnimal(Long animalId) {
        logger.debug("Buscando histórico de saúde do animal ID: {}", animalId);

        try {
            if (!animalRepository.existsById(animalId)) {
                logger.warn("Animal não encontrado ao buscar histórico - ID: {}", animalId);
                throw new DomainNotFoundException("Animal não encontrado: ID " + animalId);
            }

            List<HistoricoSaudeDto> historicos = historicoSaudeRepository.findByAnimalIdOrderByDataEventoDesc(animalId)
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            logger.debug("Encontrados {} registros de histórico para animal ID: {}", historicos.size(), animalId);
            return historicos;

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao buscar histórico: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar histórico do animal ID: {}", animalId, e);
            throw new RuntimeException("Erro ao buscar histórico de saúde", e);
        }
    }

    @Transactional(readOnly = true)
    public HistoricoSaudeDto getHistoricoById(Long id) {
        logger.debug("Buscando histórico de saúde ID: {}", id);

        try {
            HistoricoSaude historico = historicoSaudeRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Histórico de saúde não encontrado - ID: {}", id);
                        return new DomainNotFoundException("Histórico de saúde não encontrado: ID " + id);
                    });

            return mapToDto(historico);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao buscar histórico: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar histórico ID: {}", id, e);
            throw new RuntimeException("Erro ao buscar histórico de saúde", e);
        }
    }

    @Transactional
    public HistoricoSaudeDto updateHistoricoSaude(Long id, HistoricoSaudeDto dto) {
        logger.info("Atualizando histórico de saúde ID: {}", id);

        try {
            HistoricoSaude historico = historicoSaudeRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Histórico de saúde não encontrado para atualização - ID: {}", id);
                        return new DomainNotFoundException("Histórico de saúde não encontrado: ID " + id);
                    });

            historico.setTipoEvento(dto.getTipoEvento());
            historico.setDescricao(dto.getDescricao());
            historico.setCastrado(dto.getCastrado());

            HistoricoSaude updated = historicoSaudeRepository.save(historico);
            logger.info("Histórico de saúde atualizado com sucesso - ID: {}, Tipo: {}",
                    id, updated.getTipoEvento());

            return mapToDto(updated);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao atualizar histórico: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar histórico ID: {}", id, e);
            throw new RuntimeException("Erro ao atualizar histórico de saúde", e);
        }
    }

    @Transactional
    public void deleteHistoricoSaude(Long id) {
        logger.info("Deletando histórico de saúde ID: {}", id);

        try {
            if (!historicoSaudeRepository.existsById(id)) {
                logger.warn("Histórico de saúde não encontrado para exclusão - ID: {}", id);
                throw new DomainNotFoundException("Histórico de saúde não encontrado: ID " + id);
            }

            historicoSaudeRepository.deleteById(id);
            logger.info("Histórico de saúde deletado com sucesso - ID: {}", id);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao deletar histórico: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar histórico ID: {}", id, e);
            throw new RuntimeException("Erro ao deletar histórico de saúde", e);
        }
    }

    private HistoricoSaudeDto mapToDto(HistoricoSaude historico) {
        return HistoricoSaudeDto.builder()
                .id(historico.getId())
                .idAnimal(historico.getAnimal().getId())
                .tipoEvento(historico.getTipoEvento())
                .descricao(historico.getDescricao())
                .castrado(historico.getCastrado())
                .dataEvento(historico.getDataEvento())
                .build();
    }
}

