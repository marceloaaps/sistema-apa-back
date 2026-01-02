package com.apa.back.core.use_cases.animal;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.Vacinacao;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.core.domain.repositories.VacinacaoRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.presentation.v1.dtos.animal.VacinacaoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacinacaoUseCase {

    private final VacinacaoRepository vacinacaoRepository;
    private final AnimalRepository animalRepository;

    public VacinacaoUseCase(VacinacaoRepository vacinacaoRepository, AnimalRepository animalRepository) {
        this.vacinacaoRepository = vacinacaoRepository;
        this.animalRepository = animalRepository;
    }


    @Transactional
    public VacinacaoDto createVacinacao(VacinacaoDto dto) {
        Animal animal = animalRepository.findById(dto.getIdAnimal())
                .orElseThrow(() -> new DomainNotFoundException("Animal não encontrado: ID " + dto.getIdAnimal()));

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
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<VacinacaoDto> getVacinacoesByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new DomainNotFoundException("Animal não encontrado: ID " + animalId);
        }

        return vacinacaoRepository.findByAnimalIdOrderByDataAplicacaoDesc(animalId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VacinacaoDto getVacinacaoById(Long id) {
        Vacinacao vacinacao = vacinacaoRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Vacinação não encontrada: ID " + id));

        return mapToDto(vacinacao);
    }

    @Transactional(readOnly = true)
    public List<VacinacaoDto> getVacinacoesVencidas() {
        return vacinacaoRepository.findByValidadeBefore(LocalDate.now())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public VacinacaoDto updateVacinacao(Long id, VacinacaoDto dto) {
        Vacinacao vacinacao = vacinacaoRepository.findById(id)
                .orElseThrow(() -> new DomainNotFoundException("Vacinação não encontrada: ID " + id));

        vacinacao.setNomeVacina(dto.getNomeVacina());
        vacinacao.setDataAplicacao(dto.getDataAplicacao());
        vacinacao.setDose(dto.getDose());
        vacinacao.setValidade(dto.getValidade());
        vacinacao.setVeterinario(dto.getVeterinario());
        vacinacao.setObservacoes(dto.getObservacoes());

        Vacinacao updated = vacinacaoRepository.save(vacinacao);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteVacinacao(Long id) {
        if (!vacinacaoRepository.existsById(id)) {
            throw new DomainNotFoundException("Vacinação não encontrada: ID " + id);
        }
        vacinacaoRepository.deleteById(id);
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

