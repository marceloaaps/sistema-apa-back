package com.apa.back.core.use_cases.animal;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.HistoricoSaude;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.core.domain.repositories.HistoricoSaudeRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.presentation.v1.dtos.animal.HistoricoSaudeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoSaudeUseCase {

    private final HistoricoSaudeRepository historicoSaudeRepository;
    private final AnimalRepository animalRepository;

    public HistoricoSaudeUseCase(HistoricoSaudeRepository historicoSaudeRepository,
                                  AnimalRepository animalRepository) {
        this.historicoSaudeRepository = historicoSaudeRepository;
        this.animalRepository = animalRepository;
    }

    @Transactional
    public HistoricoSaudeDto createHistoricoSaude(HistoricoSaudeDto dto) {
        Animal animal = animalRepository.findById(dto.getIdAnimal())
                .orElseThrow(() -> new DomainNotFoundException("Animal não encontrado: ID " + dto.getIdAnimal()));

        HistoricoSaude historicoSaude = new HistoricoSaude(
                animal,
                dto.getTipoEvento(),
                dto.getDescricao(),
                dto.getCastrado()
        );

        HistoricoSaude saved = historicoSaudeRepository.save(historicoSaude);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<HistoricoSaudeDto> getHistoricoByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new DomainNotFoundException("Animal não encontrado: ID " + animalId);
        }

        return historicoSaudeRepository.findByAnimalIdOrderByDataEventoDesc(animalId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistoricoSaudeDto getHistoricoById(Long id) {
        HistoricoSaude historico = historicoSaudeRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Histórico de saúde não encontrado: ID " + id));

        return mapToDto(historico);
    }

    @Transactional
    public HistoricoSaudeDto updateHistoricoSaude(Long id, HistoricoSaudeDto dto) {
        HistoricoSaude historico = historicoSaudeRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Histórico de saúde não encontrado: ID " + id));

        historico.setTipoEvento(dto.getTipoEvento());
        historico.setDescricao(dto.getDescricao());
        historico.setCastrado(dto.getCastrado());

        HistoricoSaude updated = historicoSaudeRepository.save(historico);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteHistoricoSaude(Long id) {
        if (!historicoSaudeRepository.existsById(id)) {
            throw new DomainNotFoundException("Histórico de saúde não encontrado: ID " + id);
        }
        historicoSaudeRepository.deleteById(id);
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

