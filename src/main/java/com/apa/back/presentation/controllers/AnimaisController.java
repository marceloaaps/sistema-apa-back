package com.apa.back.presentation.controllers;

import com.apa.back.core.domain.entities.Animais;
import com.apa.back.core.domain.repositories.AnimaisRepository;
import com.apa.back.presentation.dtos.AnimaisDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/animais")
public class AnimaisController {

    private final AnimaisRepository animaisRepository;

    public AnimaisController(AnimaisRepository animaisRepository) {
        this.animaisRepository = animaisRepository;
    }

    @PostMapping
    public ResponseEntity<Animais> createAnimal(@RequestBody AnimaisDTO animaisDTO) {
        Animais animais = new Animais(
                null,
                animaisDTO.getNome(),
                animaisDTO.getIdade(),
                animaisDTO.getRaca(),
                animaisDTO.getIdSaude(),
                animaisDTO.getComportamento(),
                animaisDTO.getHistorico(),
                animaisDTO.getDataCadastro(),
                animaisDTO.getDisponivelParaAdocao()
        );

        Animais savedAnimais = animaisRepository.save(animais);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimais);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animais> updateAnimal(@PathVariable Long id, @RequestBody AnimaisDTO animaisDTO) {
        Optional<Animais> existingAnimal = animaisRepository.findById(id);

        if (!existingAnimal.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Animais animal = existingAnimal.get();
        animal.setNome(animaisDTO.getNome());
        animal.setIdade(animaisDTO.getIdade());
        animal.setRaca(animaisDTO.getRaca());
        animal.setIdSaude(animaisDTO.getIdSaude());
        animal.setComportamento(animaisDTO.getComportamento());
        animal.setHistorico(animaisDTO.getHistorico());
        animal.setDataCadastro(animaisDTO.getDataCadastro());
        animal.setDisponivelParaAdocao(animaisDTO.getDisponivelParaAdocao());

        Animais updatedAnimal = animaisRepository.save(animal);

        return ResponseEntity.status(HttpStatus.OK).body(updatedAnimal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id, @RequestParam("deletadoPor") Integer deletadoPor) {
        Optional<Animais> existingAnimal = animaisRepository.findById(id);

        if (!existingAnimal.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Animais animal = existingAnimal.get();
        animal.setDisponivelParaAdocao(false);
        animal.setDeletadoEm(LocalDate.now());
        animal.setDeletadoPor(deletadoPor);

        animaisRepository.save(animal);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
    }

    // Restauração do animal
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Animais> restoreAnimal(@PathVariable Long id) {
        Optional<Animais> existingAnimal = animaisRepository.findById(id);

        if (!existingAnimal.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Animais animal = existingAnimal.get();
        animal.setDeletadoEm(null);
        animal.setDeletadoPor(null);
        animal.setDisponivelParaAdocao(true);

        Animais restoredAnimal = animaisRepository.save(animal);

        return ResponseEntity.status(HttpStatus.OK).body(restoredAnimal);
    }
}
